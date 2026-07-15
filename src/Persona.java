public class Persona {
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta){
        this.nome=nome;
        this.cognome=cognome;
        this.telefono=telefono;
        this.indirizzo=indirizzo;
        this.eta=eta;
    }

    public void setNome(String nome){
        this.nome=nome;
    }

    public void setCognome(String cognome){
        this.cognome=cognome;
    }

    public void setIndirizzo(String indirizzo){
        this.indirizzo=indirizzo;
    }

    public void setTelefono(String telefono){
        this.telefono=telefono;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCognome(){
        return cognome;
    }

    public String getIndirizzo(){
        return this.indirizzo;
    }

    public String getTelefono(){
        return this.telefono;
    }

    public int getEta(){
        return this.eta;
    }
}
