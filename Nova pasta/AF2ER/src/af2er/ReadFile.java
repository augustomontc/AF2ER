/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package af2er;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Augusto-PC
 */
public class ReadFile {
    
    private String text = "";
    
    public String string(){
        return text;
    }
    
    public ReadFile(String FILENAME) {        
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))){
            String sCurrentLine;
            
            while ((sCurrentLine = br.readLine()) != null) {
                text += sCurrentLine;
            }
               
        } catch (IOException e) {
            System.out.println("ReadFile error");
        }
    }
    
}
