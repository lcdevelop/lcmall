package com.lcsays.lcmall.api.controller.evertabs;

import com.lcsays.lcmall.api.config.OSSConfiguration;
import com.lcsays.lcmall.api.utils.OssUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import net.sf.image4j.codec.ico.ICODecoder;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static com.lcsays.lcmall.api.constant.Names.REDIS_FAVICON_KEY;

/**
 * @Author: lichuang
 * @Date: 2022/4/21 08:08
 */
@Component
@Slf4j
public class JobExecutor {

    @Resource
    OSSConfiguration ossConfiguration;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "* * * * * *")
    @Async
    public void crawlFavicon() {
        while (true) {
            Object url = redisTemplate.opsForList().rightPop(REDIS_FAVICON_KEY);
            if (null == url) {
                break;
            } else {
                crawl((String) url);
            }
        }
    }

    private String getFileTypeStr(String url) {
        if (null != url) {
            if (url.startsWith("data:image")) {
                return "image-data";
            }
            String[] splits = url.split("\\.");
            if (splits.length > 0) {
                return splits[splits.length - 1];
            }
        }
        return null;
    }

    private String genFavIconFileName(URL u) {
        String file = u.getFile();
        if (file.contains("?")) {
            file = file.substring(0, file.indexOf("?"));
        }
        String ret = u.getHost() + file;
        ret = ret.replace("/", "_");
        if (ret.startsWith("_")) {
            ret = ret.substring(1);
        }
        return ret;
    }

    private String uploadIco(String url) {
        try {
            URL u = new URL(url);
            List<BufferedImage> list = ICODecoder.read(u.openStream());
            BufferedImage icoImage = list.get(0);
            ByteOutputStream bos = new ByteOutputStream();
            ImageIO.write(icoImage, "png", bos);

            String fileName = genFavIconFileName(u);
            fileName = fileName.replace(".ico", ".png");
            return OssUtils.uploadBytes(
                    bos.newInputStream(),
                    ossConfiguration.getEndpoint(),
                    ossConfiguration.getKeyId(),
                    ossConfiguration.getKeySecret(),
                    ossConfiguration.getBucketName(),
                    ossConfiguration.getFileHost(),
                    fileName
            );
        } catch (Exception e) {
            return null;
        }
    }

    private String uploadSvg(String url) {
        try {
            URL u = new URL(url);
            ByteOutputStream bos = new ByteOutputStream();

            Transcoder transcoder = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(u.openStream());
            TranscoderOutput output = new TranscoderOutput(bos);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, 128f);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, 128f);
            transcoder.transcode(input, output);

            String fileName = genFavIconFileName(u);
            fileName = fileName.replace(".svg", ".png");
            return OssUtils.uploadBytes(
                    bos.newInputStream(),
                    ossConfiguration.getEndpoint(),
                    ossConfiguration.getKeyId(),
                    ossConfiguration.getKeySecret(),
                    ossConfiguration.getBucketName(),
                    ossConfiguration.getFileHost(),
                    fileName
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String uploadOther(String url) {
        try {
            URL u = new URL(url);
            String fileName = genFavIconFileName(u);
            return OssUtils.uploadBytes(
                    u.openStream(),
                    ossConfiguration.getEndpoint(),
                    ossConfiguration.getKeyId(),
                    ossConfiguration.getKeySecret(),
                    ossConfiguration.getBucketName(),
                    ossConfiguration.getFileHost(),
                    fileName
            );
        } catch (Exception e) {
            return null;
        }
    }

    private void crawl(String url) {
        log.info(String.valueOf(url));
        String fileTypeStr = getFileTypeStr(url);
        String ossUrl;
        if (fileTypeStr.equals("ico")) {
            ossUrl = uploadIco(url);
            if (null == ossUrl) {
                ossUrl = uploadOther(url);
            }
        } else if (fileTypeStr.equals("image-data")) {
            ossUrl = null;
        } else if (fileTypeStr.equals("svg")) {
            ossUrl = uploadSvg(url);
        } else {
            ossUrl = uploadOther(url);
        }
        if (null != ossUrl) {
            log.info(ossUrl);
            redisTemplate.opsForValue().set(url, ossUrl, Duration.ofDays(30));
        }
    }

}
