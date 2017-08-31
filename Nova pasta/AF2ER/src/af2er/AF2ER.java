/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package af2er;

import af2er.ReadJson.AF;
import java.util.ArrayList;

/**
 *
 * @author Augusto-PC
 * 
 * Trabalho de LFA - 2017/1
 * Alunos:
 *      Augusto Monteiro de Castro Silva Costa
 *      Guilherme Tadeu Borges Coutinho
 * 
 * * O arquivo lido deve ser escolhido na classe ReadFile *
 * 
 */
public class AF2ER {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        if(args.length != 0){
            System.out.println("Usar: verificador [AFN]");
            System.exit(0);
        }
        
        ReadFile file = new ReadFile(args[0]);
        String text = file.string();
        
        ReadJson json = new ReadJson(text);
        AF automato = json.getAF();
        ArrayList<String> ordem = json.getOrdem();
        
        executa(automato, ordem);
        if(automato.transicoes.size()>0) System.out.println(automato.transicoes.get(0).get(1));
        else System.out.println("ERRO");
    }
    
    private static void executa(AF automato, ArrayList<String> ordem){
        passo1(automato);
        passo2(automato);
        passo3(automato, ordem);
    }
    
    private static void passo1(AF automato){
        // 1. Adicionar apenas um estado inicial e um final
        int i;
        String str_i = "i", str_f = "f";
        // Adiciona os estados inicial e final
        automato.estados.add(0, str_i);
        automato.estados.add(automato.estados.size(), str_f);
        automato.iniciais.add(automato.iniciais.size(), str_i);
        automato.finais.add(automato.finais.size(), str_f);
        
        // Cria transições lambda para todos estados iniciais
        for(i=0; i<automato.iniciais.size(); i++){
            if(!automato.iniciais.get(0).equals(str_i)){
                ArrayList<String> element = new ArrayList<>();
                    element.add(0, str_i);
                    element.add(1, "#");
                    element.add(2, automato.iniciais.get(0));
                automato.iniciais.remove(0);
                automato.transicoes.add(automato.transicoes.size(), element);
            }
        }
        
        // Cria transições lambda para todos estados finais
        for(i=0; i<automato.finais.size(); i++){
            if(!automato.finais.get(0).equals(str_f)){
                ArrayList<String> element = new ArrayList<>();
                    element.add(0, automato.finais.get(0));
                    element.add(1, "#");
                    element.add(2, str_f);
                automato.finais.remove(0);
                automato.transicoes.add(automato.transicoes.size(), element);
            }
        }
    }
    
    // se todas transições de i forem apenas para i, remove
    
    private static void passo2(AF automato){        
        // Remove transições inúteis
        int inutil = 1;
        String estado;
        for(int e=1; e<automato.estados.size()-1; e++){
            estado = automato.estados.get(e);
            for(int i=0; i<automato.transicoes.size(); i++){
                if(automato.transicoes.get(i).get(0).equals(estado)){
                    if(!automato.transicoes.get(i).get(2).equals(automato.transicoes.get(i).get(0)))
                        inutil = 0;
                        
                }
            }
            if(inutil==1){
                for(int j=0; j<automato.transicoes.size(); j++){
                    if(automato.transicoes.get(j).get(0).equals(estado)  ||
                            automato.transicoes.get(j).get(2).equals(estado)){
                        automato.transicoes.remove(j);
                        j--;
                    }
                }
            } else 
                inutil=1;
        }
        
        // 2. Substituir todas as transições e para e' por conjunções em ER
        String ei1, ei2, ef1, ef2, str1, str2;
        for(int i=0; i<automato.transicoes.size(); i++){
            for(int j=i; j<automato.transicoes.size(); j++){
                // Estados iniciais
                ei1 = automato.transicoes.get(i).get(0);
                ei2 = automato.transicoes.get(j).get(0);
                // Estados finais
                ef1 = automato.transicoes.get(i).get(2);
                ef2 = automato.transicoes.get(j).get(2); 
                // Transicoes
                str1 = automato.transicoes.get(i).get(1);
                str2 = automato.transicoes.get(j).get(1);
                                
                if(i!=j
                    && (ei1 == null ? ei2 == null : ei1.equals(ei2))
                    && (ef1 == null ? ef2 == null : ef1.equals(ef2))){
                        String new_str,x;
                        
                        if(str1.startsWith("(") && str1.endsWith(")")){
                            // Se existe parenteses apenas no inicio e final
                            // ignora os parenteses para nao ficar ((a+b))*
                            x = str1.substring(1, str1.length()-1);
                            if(!x.contains("(")) str1=x;
                        }
                        if(str2.startsWith("(") && str2.endsWith(")")){
                            x = str2.substring(1, str2.length()-1);
                            if(!x.contains("(")) str2=x;
                        }
                        
                        // # vem primeiro na conjunção
                        if(str2.equals("#")) new_str = "(" + str2 + "+" + str1 + ")";
                        else new_str = "(" + str1 + "+" + str2 + ")";

                        automato.transicoes.get(i).set(1, new_str);
                        automato.transicoes.remove(j);
                        i=0; j=0; // Recomeça os loops
                }
                                    
            }
        }
    }
    
    private static void passo3(AF automato, ArrayList<String> ordem){
        // 3. Remover os estados na ordem
        /*pega transição da ordem
        concatena as que entram com as que saem
        se v=# ignora
        se |v|>0 usa ()
        passo 2
        */
        int i, j, k;
        ArrayList<Integer> in  = new ArrayList<>();
        ArrayList<Integer> out = new ArrayList<>();
        Integer loop = null;
        
        ArrayList<String> element;
        
        String str, str_in, str_out, str_loop = "";
        for(k=0; k<ordem.size(); k++){
            String t_atual = ordem.get(k);

            // Verifica quais sao as transições chegando, saindo e em loop
            for(i=0; i<automato.transicoes.size(); i++){
                if(automato.transicoes.get(i).get(0).equals(t_atual) &&
                        automato.transicoes.get(i).get(2).equals(t_atual))
                    loop = i;
                else
                if(automato.transicoes.get(i).get(0).equals(t_atual))
                    out.add(i);
                else
                if(automato.transicoes.get(i).get(2).equals(t_atual))
                    in.add(i);            
            }
            
            // Concatena as transições
            for(i=0; i<in.size(); i++){
                for(j=0; j<out.size(); j++){    
                    str_in = automato.transicoes.get(in.get(i)).get(1);
                    str_out = automato.transicoes.get(out.get(j)).get(1);
                    if(loop!=null) str_loop = automato.transicoes.get(loop).get(1);
                    if(str_loop.equals("#")) str_loop="";
                    if(str_in.equals("#")) str_in="";
                    if(str_out.equals("#")) str_out="";

                    if(str_loop.length()==1 ||
                            (str_loop.startsWith("(") && str_loop.endsWith(")")))
                        str_loop = str_loop + "*";
                    else if(!str_loop.equals(""))
                        str_loop = "(" + str_loop + ")*";

                    str = str_in + str_loop + str_out;
                    
                    // Cria nova transição
                    element = new ArrayList<>();
                    element.add(0, automato.transicoes.get(in.get(i)).get(0));
                    element.add(1, str);
                    element.add(2, automato.transicoes.get(out.get(j)).get(2));
                    
                    // Adiciona a nova transição
                    automato.transicoes.add(automato.transicoes.size(), element); 
                    // Reaplica o passo 2 para as novas transições
                    passo2(automato);
                }
            }

            // Remove as transições antigas
            for(i=0; i<automato.transicoes.size(); i++){
                if(automato.transicoes.get(i).get(0).equals(t_atual) ||
                        automato.transicoes.get(i).get(2).equals(t_atual)){
                    automato.transicoes.remove(i);
                    i--; // Volta na lista
                }
            }
        
            // Limpa os arrays
            in.clear();
            out.clear();
        }   
    }
}
