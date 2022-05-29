package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;

import fonctions.Fonctions;

import static fonctions.Fonctions.*;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Test")
public class TestFonctions {

    @Test
    @DisplayName("Test de la correspondance SHA256 (verifié en ligne)")
    public void shaTest(){
        String sha = sha256("maison");
        String result = "ffd73d6baeb4efb7922347d91daf780335701edb022f86e7363073f714d7e8b0";
        assertEquals(result,sha);
    }

    @Test
    @DisplayName("Test nombre de caractère SHA256")
    public void nbCharSha(){
    	assertEquals(64, Fonctions.sha256("Ce hash doit faire 64 caractères").length());
    }
    

    @Test
    @DisplayName("date valide")
    public void dateValideTest(){
        String date = "2022-03-03";
        assertTrue(dateValide(date));
    }

    @Test
    @DisplayName("date inverse")
    public void dateFormatInverse(){
        String date = "03-03-2022";
        assertFalse(dateValide(date));
    }

    @Test
    @DisplayName("Mauvais format")
    public void dateFormatMauvais(){
        String date = "03032022";
        assertFalse(dateValide(date));
    }

    @Test
    @DisplayName("Mois impossible")
    public void dateMoisImpossible(){
        String date = "2022-13-02";
        assertFalse(dateValide(date));
    }

    @Test
    @DisplayName("Jour impossible")
    public void dateJourImpossible(){
        String date = "2022-03-32";
        assertFalse(dateValide(date));
    }

    @Test
    @DisplayName("heure valide")
    public void heureValideTest(){
        String heure = "22:30";
        assertTrue(heureValide(heure));
    }

    @Test
    @DisplayName("heure format non valide")
    public void heureFormatNonValideTest(){
        String heure = "2230";
        assertFalse(heureValide(heure));
    }

    @Test
    @DisplayName("heure non valide")
    public void heureNonValideTest(){
        String heure = "26:30";
        assertFalse(heureValide(heure));
    }

    @Test
    @DisplayName("minute non valide")
    public void minuteNonValideTest(){
        String heure = "23:61";
        assertFalse(heureValide(heure));
    }

    @Test
    @DisplayName("ajout de 0 minutes")
    public void AjoutZeroMinutesTest(){
        assertEquals(addMinutestoDate("2022-03-03 18:33",0),"2022-03-03 18:33");
    }

    @Test
    @DisplayName("mauvais format de date")
    public void addMinuteMauvaisFormatTest(){
        assertEquals(addMinutestoDate("azerazerazer",60),"2000-01-01 01:00");
    }

    @Test
    @DisplayName("changement de mois")
    public void changeMoisTest(){
        assertEquals(addMinutestoDate("2022-03-31 23:30",60),"2022-04-01 00:30");
    }

    @Test
    @DisplayName("changement de jour")
    public void changeJourTest(){
        assertEquals(addMinutestoDate("2022-03-30 23:30",60),"2022-03-31 00:30");
    }

    @Test
    @DisplayName("changement d'annee")
    public void changeAnneeTest(){
        assertEquals(addMinutestoDate("2022-12-31 23:30",60),"2023-01-01 00:30");
    }



}
