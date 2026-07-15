import javax.swing.*;
import java.awt.*;

public class EditorPersona extends JFrame {

    private JTextField campoNome;
    private JTextField campoCognome;
    private JTextField campoIndirizzo;
    private JTextField campoTelefono;
    private JTextField campoEta;

    private JButton salva;
    private JButton annulla;

    private Persona persona;
    private final PersonaSalvataListener listener;

    public EditorPersona(PersonaSalvataListener listener) {
        this.listener = listener;
        inizializzaInterfaccia();
    }

    public EditorPersona(Persona persona, PersonaSalvataListener listener) {
        this(listener);
        this.persona = persona;
        caricaPersona(persona);
    }

    private void inizializzaInterfaccia() {
        setTitle("Editor persona");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pannelloCampi = new JPanel(new GridLayout(5, 2, 10, 10));

        campoNome = new JTextField(20);
        campoCognome = new JTextField(20);
        campoIndirizzo = new JTextField(20);
        campoTelefono = new JTextField(20);
        campoEta = new JTextField(20);

        pannelloCampi.add(new JLabel("Nome:"));
        pannelloCampi.add(campoNome);

        pannelloCampi.add(new JLabel("Cognome:"));
        pannelloCampi.add(campoCognome);

        pannelloCampi.add(new JLabel("Indirizzo:"));
        pannelloCampi.add(campoIndirizzo);

        pannelloCampi.add(new JLabel("Telefono:"));
        pannelloCampi.add(campoTelefono);

        pannelloCampi.add(new JLabel("Età:"));
        pannelloCampi.add(campoEta);

        salva = new JButton("Salva");
        annulla = new JButton("Annulla");

        annulla.addActionListener(e -> dispose());
        salva.addActionListener(e -> salvaPersona());

        JPanel pannelloPulsanti =
                new JPanel(new FlowLayout(FlowLayout.RIGHT));

        pannelloPulsanti.add(salva);
        pannelloPulsanti.add(annulla);

        pannelloCampi.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 10, 15)
        );

        add(pannelloCampi, BorderLayout.CENTER);
        add(pannelloPulsanti, BorderLayout.SOUTH);

        pack();
    }

    private void caricaPersona(Persona persona) {
        campoNome.setText(persona.getNome());
        campoCognome.setText(persona.getCognome());
        campoIndirizzo.setText(persona.getIndirizzo());
        campoTelefono.setText(persona.getTelefono());
        campoEta.setText(String.valueOf(persona.getEta()));
    }

    private Persona leggiPersonaDaiCampi() {
        String nome = campoNome.getText().trim();
        String cognome = campoCognome.getText().trim();
        String indirizzo = campoIndirizzo.getText().trim();
        String telefono = campoTelefono.getText().trim();
        int eta= Integer.parseInt(campoEta.getText().trim());

        return new Persona(nome, cognome, indirizzo, telefono, eta);
    }

    private void salvaPersona() {
        if(!campiValidi()){
            return;
        }

        if (persona == null) {
            creaNuovaPersona();
        } else {
            modificaPersonaEsistente();
        }
        listener.personaSalvata(persona);
        dispose();
    }

    private void creaNuovaPersona() {
        this.persona = leggiPersonaDaiCampi();

        System.out.println("Creata nuova persona: " + persona.getNome() + " " + persona.getCognome());
    }

    private void modificaPersonaEsistente() {
        Persona nuoviDati = leggiPersonaDaiCampi();

        persona.setNome(nuoviDati.getNome());
        persona.setCognome(nuoviDati.getCognome());
        persona.setIndirizzo(nuoviDati.getIndirizzo());
        persona.setTelefono(nuoviDati.getTelefono());
        persona.setEta(nuoviDati.getEta());

        System.out.println("Modificata persona: " + persona.getNome() + " " + persona.getCognome());
    }

    private boolean campiValidi() {
        String nome = campoNome.getText().trim();
        String cognome = campoCognome.getText().trim();
        String indirizzo = campoIndirizzo.getText().trim();
        String telefono = campoTelefono.getText().trim();
        String testoEta = campoEta.getText().trim();

        if (nome.isEmpty() || cognome.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || testoEta.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Compila tutti i campi.", "Dati mancanti", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int eta = Integer.parseInt(testoEta);
            if (eta < 0) {JOptionPane.showMessageDialog(this, "L'età non può essere negativa.", "Età non valida", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "L'età deve essere un numero intero.", "Età non valida", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }
}