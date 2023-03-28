/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chattcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

class Gerenciador implements Runnable {
    //Responsável por gerenciar múltiplas instâncias
    
    //É necessário criar um array para cada instância dessa classe. 
    //o objetivo desse array é observar cada conexão para que um cliente
    //possa mandar uma mensagem para outros clientes
    public static ArrayList<Gerenciador> clientes = new ArrayList<>(); 
    private Socket socket; 
    //Usado para ler as mensagens
    private BufferedReader receber; 
    //Usado para enviar as mesagens
    private BufferedWriter enviar; 
    private String userName;

    public Gerenciador(Socket socket) throws IOException {
        this.socket = socket;
        this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.enviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.userName = receber.readLine(); 
        clientes.add(this);
        
        transmitir("Servidor: W3ELR75 User: " + userName + " has been conected");
    }
    
    public void transmitir(String mensagem) throws IOException {
        for (Gerenciador cliente : clientes) {
            if(!cliente.userName.equals(userName))
            cliente.enviar.write(mensagem);
            cliente.enviar.newLine();
            cliente.enviar.flush();
            
        }
    }
    
    public void removerCliente() throws IOException {
        clientes.remove(this);
        transmitir(userName + " Saiu");
    }
    
    public void fecha(Socket socket, BufferedReader receber, BufferedWriter enviar) {
        try {
            if(socket != null) {
                socket.close();
            }
            if(receber != null) {
                receber.close();
            }
            if(enviar != null) {
                enviar.close();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        String mensagem; 
        while(socket.isConnected()) {
            try {
                mensagem = receber.readLine();
                transmitir(mensagem); 
            } catch (IOException e) {
                fecha(socket, receber, enviar); 
            }
        }
    }
   
}
