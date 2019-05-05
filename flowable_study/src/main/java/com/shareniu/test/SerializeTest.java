package com.shareniu.test;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializeTest {

    public static void main(String[] args) throws IOException {
        Person person=new Person();
        person.setAge(18);
        person.setName("aaa");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //Hessian的序列化输出
        HessianOutput ho = new HessianOutput(os);
        ho.writeObject(person);

        byte[] userByte = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(userByte);


        //Hessian的反序列化读取对象
        HessianInput hi = new HessianInput(is);
        Person u = (Person) hi.readObject();
        System.out.println("姓名：" + u.getName());
        System.out.println("年龄：" + u.getAge());
    }



}
