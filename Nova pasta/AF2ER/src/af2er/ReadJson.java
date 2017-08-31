package af2er;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadJson {

    /**
     *
     * @throws JSONException
     */
        
    public class AF{
        ArrayList<String> estados;
        ArrayList<String> alfabeto;
        ArrayList<ArrayList<String>> transicoes;
        ArrayList<String> iniciais;
        ArrayList<String> finais;
    }
    
    private final AF AutomatoFinito;
    private final ArrayList<String> Ordem;
    
    private AF readAF(JSONArray array){
        int i;
        AF af = new AF();
        af.estados   = Json2Array(array.optJSONArray(0));
        af.alfabeto    = Json2Array(array.optJSONArray(1));
        af.iniciais  = Json2Array(array.optJSONArray(3));
        af.finais    = Json2Array(array.optJSONArray(4));
        JSONArray transArray   =  array.optJSONArray(2);
        
        af.transicoes = new ArrayList<>();
        for(i=0; i<transArray.length(); ++i){
            af.transicoes.add(Json2Array(transArray.optJSONArray(i)));
        }
        
        return af;
    }
    
    private ArrayList<String> Json2Array(JSONArray json){
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < json.length(); ++i) {
            array.add(json.optString(i));
        }
        return array;
    }
    
    public ReadJson(String json_str) throws JSONException {
        // JSON
        JSONObject my_obj = new JSONObject(json_str);
        
        // AUTOMATO FINITO
        JSONArray afArray = my_obj.getJSONArray("af");
        AutomatoFinito = readAF(afArray);
        
        // ORDEM
        JSONArray ordemArray = my_obj.optJSONArray("r");
        Ordem = Json2Array(ordemArray);
    }
    
    public AF getAF(){
        return this.AutomatoFinito;
    }
    
    public ArrayList<String> getOrdem(){
        return this.Ordem;
    }

    public void printOr(ArrayList<String> ordem){
        System.out.println("ordem: " + Ordem);	
    }
    
    public void printAF(AF af){
        System.out.println("estados: " + (af.estados) + "\n" +
                           "alfabeto: " + (af.alfabeto) + "\n" +
                           "transicoes: ");
        for(int i=0; i<af.transicoes.size(); i++)
            System.out.println("\t" + i + ": "+ af.transicoes.get(i));
        System.out.println("iniciais: " + (af.iniciais) + "\n" +
                           "finais: " + (af.finais));        

    }
}