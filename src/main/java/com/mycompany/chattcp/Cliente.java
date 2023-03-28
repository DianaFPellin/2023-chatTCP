/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chattcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Aluno
 */
public class Cliente {
    private Socket socket;
    private String userName;
    private BufferedReader receber;
    private BufferedWriter enviar; 

    public Cliente(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.userName = userName;
            this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.enviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            fecha(socket, receber, enviar);
        }
    }
    
    //Envia uma mensagem para o gerenciador que está esperando.
    public void enviar() {
        try {
            enviar.write(userName);
            enviar.newLine();
            enviar.flush();
            
            Scanner scanner = new Scanner(System.in); 
            while (socket.isConnected()) {
                String msg = scanner.nextLine();
                enviar.write(userName + ": " + msg);
            }
        } catch (Exception e) {
             fecha(socket, receber, enviar);
        }
    }
    
    public void receber() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               String mensagemRecebida;
               while(socket.isConnected()) {
                   try {
                      mensagemRecebida = receber.readLine();
                       System.out.println(mensagemRecebida);
                   } catch (IOException e) {
                        fecha(socket, receber, enviar);
                   }
               }
            } 
        }).start(); //Precisamos chamar o objeto Thread usando o método start
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
    
       public static void main(String[] args) throws IOException {
           Scanner scanner = new Scanner(System.in);
           
           System.out.println("Digite um user name: ");
           String userName = scanner.nextLine();
           
           Socket socket = new Socket("localhost", 8080);
           Cliente cliente = new Cliente(socket, userName);
           
           cliente.receber(); 
           cliente.enviar();
       }
    
}
