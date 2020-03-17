package com.example.cripto_photoaffix.Authenticators;

import com.example.cripto_photoaffix.Security.EncryptedFiles.EncryptedFile;

public interface Authenticator {

    /**
     * Al identificarse se llama a este metodo.
     */
    public void authenticate();

    /**
     * Inicializa la autenticacion, en especial para las biometricas que corren en segundo plano.
     */
    public void initialize();

    /**
     * Determina si el dispositivo puede usar este autenticador.
     * @return Si el dispositivo tiene disponible este autenticador.
     */
    public boolean canBeUsed();

    /**
     * Encripta informacion y la retorna en forma de un archivo encriptado.
     * @param data Informacion a encriptar.
     * @return Archivo encriptado con la informacion obviamente encriptada.
     */
    public EncryptedFile encrypt(String data);

    /**
     * Decripta la informacion en un archivo encriptado y la retorna en forma de String. Notese que
     * el autenticador debe haberse autenticado anteriormente para poder realizar esto.
     * @param file Archivo encriptado a desencriptar.
     * @return String con la informacion desencriptada.
     */
    public String decrypt(EncryptedFile file);

    /**
     * Retorna la contraseña "universal".
     * @return Contraseña universal.
     */
    public String getFinalPassword();

    /**
     * Determina si estan todos los archivos correspondientes con este autenticador listos.
     * @return True si estan listos, False si no.
     */
    public boolean filesReady();
}
