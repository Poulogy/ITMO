import classes.people.Character;
import classes.places.*;
import classes.people.*;
import enums.Profesion;

public class Main {
    public static void main(String[] args) {

        Hospital hospital = new Hospital("больница");
        NewspaperOffice newspaperoffice = new NewspaperOffice("газетный оффис");
        PoliceStation policestation = new PoliceStation("полицейский участок");
        PoppyPlace poppyPlace = new PoppyPlace("место Маковки");
        ShutiloPlace shutiloPlace = new ShutiloPlace("место Шутило и Коржика");
        Compress drCompress = new Compress("Компрессик", hospital);
        Shutilo shutilo = new Shutilo("Шутило", hospital);
        Korjik biscuit = new Korjik("Коржик", hospital);
        Perishkin feather = new Perishkin("Пёрышкин", newspaperoffice);
        Svistulkin policeWistler = new Svistulkin("Свистулькин", null, 1);
        if (Math.random() <= 0.5) {
            policeWistler.moveToRoom(hospital);
        }
        Makovka babyPoppy = new Makovka("Маковка", poppyPlace);
        Karaulkin policeKaraulkin = new Karaulkin("Караулькин", policestation);
        policeWistler.getJob(Profesion.POLICE);
        policeKaraulkin.getJob(Profesion.POLICE);
        drCompress.getJob(Profesion.DOCTOR);
        feather.getJob(Profesion.REPORTER);
        System.out.println("Start");
        shutilo.moveToRoom(shutiloPlace);
        biscuit.moveToRoom(shutiloPlace);
        drCompress.think();
        String wistlerLoc = policeWistler.getLoc();
        int missing = 1;
        int a = 0;
        if (wistlerLoc == String.valueOf(hospital)) {
            drCompress.call(newspaperoffice, policeWistler);
            missing = 0;
            Character[] characters = new Character[1];
            characters[0] = drCompress;
            feather.findOut(characters, policeWistler);
        } else {
            drCompress.call(newspaperoffice, policeWistler);
            a = missing;
        }
        feather.moveToRoom(hospital);
        if (missing == 1){
            feather.ask(drCompress);
        }
        else {
            feather.ask(drCompress);
            feather.ask(policeWistler);
        }
        feather.moveToRoom(shutilo.getpl());
        feather.ask(shutilo);
        feather.ask(biscuit);
        missing = biscuit.know(String.valueOf(policeWistler), biscuit, missing);
        missing  = shutilo.know(String.valueOf(policeWistler), shutilo, missing);

        if (missing == 0 & a == 1) {

            Character[] characters = new Character[2];
            characters[0] = shutilo;
            characters[1] = biscuit;
            feather.findOut(characters, policeWistler);
            a = 0;
        }
        feather.moveToRoom(babyPoppy.getpl());
        feather.ask(babyPoppy);
        missing = babyPoppy.know(String.valueOf(policeWistler), babyPoppy, missing);

        if (missing == 0 & a == 1) {

            Character[] characters = new Character[1];
            characters[0] = babyPoppy;
            feather.findOut(characters, policeWistler);
        }
        feather.moveToRoom(policestation);
        feather.look(policestation);
        if (missing == 1) {
            feather.say(policeKaraulkin);
            System.out.println(policeWistler+ " всё ещё пропавший");
        }
        else {
            feather.say(policeKaraulkin);
            System.out.println(policeWistler+ " нашёлся");
        }


    }


}
