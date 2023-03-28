/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chattcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server; 
    
    //Objeto responsável por ouvir os clientes e criar os sockets;
    public Server(ServerSocket server) {
        this.server = server; 
    }
    
    public void iniciarSessao() throws IOException {
        while(!server.isClosed()) {
            //A partir desse ponto o servidor aguarda uma conexão
            //quando um cliente se conecta o método a accept cria um socket para o cliente. 
            Socket conexao = server.accept(); 
            System.out.println("Um cliente se conectou!");
            Gerenciador gerenciadorCliente = new Gerenciador(conexao); 
            
            Thread t = new Thread(gerenciadorCliente);
            t.start();
        }
    }
    
    public void encerrarSessao() throws IOException {
        if(server != null) {
            server.close();
        }
    }
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); 
        Server serverConstructor = new Server(serverSocket);
        System.out.println("Servidor iniciado");
        serverConstructor.iniciarSessao();
        
    }
    
}
