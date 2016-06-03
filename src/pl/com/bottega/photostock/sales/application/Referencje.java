package pl.com.bottega.photostock.sales.application;

/**
 * Created by Dell on 2016-04-02.
 */
public class Referencje{

    public static void main(String[] args){
        Person jan = new Person("janusz");

        ZmieniaczImion zmieniacz = new ZmieniaczImion();


        System.out.println(jan.getName()); //-----> janusz
        //na czas wywołania metody zmien jest tworzona kopia referencji "jan"
        zmieniacz.zmien(jan);
        //zmieniacz zmienił obiekt wskazywany przez kopię referencji "jan"
        System.out.println(jan.getName()); //-----> ala //skutek działania linijki 17
    }
}

//==========

class ZmieniaczImion{
    public void zmien(Person person){
        person.setName("ala");//zmiana na obiekcie, który jest wkazywany przez kopię referencji
        //zmiana ALE kopii referecnji, więc nie widoczna poza tą metodą
        person = new Person("zdzichu");
    }
}

//============

class Person{
    private String name;

    public Person(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}

