import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-layout';

export default () => {
  const defaultMessage = 'lcsays.com精心打造';
  return (
    <DefaultFooter
      copyright={`2021 ${defaultMessage}`}
      links={[
        {
          key: 'lcsays',
          title: 'lcsays',
          href: 'https://www.lcsays.com',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/lcdevelop',
          blankTarget: true,
        },
      ]}
    />
  );
};
