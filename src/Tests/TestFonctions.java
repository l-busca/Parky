package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.*;
import org.junit.jupiter.api.DisplayName;

import fonctions.Fonctions;

@DisplayName("Test")
public class TestFonctions {

    @Test
    @DisplayName("Test nombre de caractère SHA256")
    public void nbCharSha(){
    	assertEquals(64, Fonctions.sha256("Ce hash doit faire 64 caractères").length());
    }
    
    @Test
    @DisplayName("Sha256 correct (verifier en ligne)")
    public void Sha256Correct(){
    	assertEquals("ffd73d6baeb4efb7922347d91daf780335701edb022f86e7363073f714d7e8b0", Fonctions.sha256("maison"));
    }
}
