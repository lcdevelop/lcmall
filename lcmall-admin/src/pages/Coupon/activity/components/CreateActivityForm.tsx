import React from "react";
import {ModalForm} from "@ant-design/pro-form";


export type CreateActivityFormProps = {
  visible: boolean;
};

const CreateActivityForm: React.FC<CreateActivityFormProps> = (props) => {
  return (
    <ModalForm
      title='创建活动'
      visible={props.visible}
    >

    </ModalForm>
  )
}

export default CreateActivityForm;
