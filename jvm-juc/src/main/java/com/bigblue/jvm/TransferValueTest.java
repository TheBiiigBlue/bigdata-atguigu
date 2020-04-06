package com.bigblue.jvm;

/**
 * @Author: TheBigBlue
 * @Description:
 * @Date: 2020/4/5
 */
public class TransferValueTest {

    public static void main(String[] args) {
        TransferValueTest test = new TransferValueTest();
        int age = 30;
        test.changeValue1(age);
        System.out.println("age ---> " + age);

        Person person = new Person("abc");
        test.changeValue2(person);
        System.out.println("personName ---> " + person.getName());

        String str = "abc";
        test.changeValue3(str);
        System.out.println("str ---> " + str);
    }

    public void changeValue1(int age) {
        age = 30;
    }

    public void changeValue2(Person person) {
        person.setName("xxx");
    }

    public void changeValue3(String str) {
        str = "xxx";
    }

    static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
