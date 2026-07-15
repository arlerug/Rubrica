import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Rubrica extends JFrame {

    private JTable tabella;
    private DefaultTableModel modelloTabella;
    private final ArrayList<Persona> listaPersone=new ArrayList<>();
    private static final String NOME_CARTELLA = "informazioni";

    public Rubrica(){
        setLayout(new BorderLayout());
        setTitle("Rubrica");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titolo= new JLabel("Rubrica");
        titolo.setFont(new Font("Arial", Font.BOLD, 24));
        titolo.setHorizontalAlignment(SwingConstants.CENTER);


        String[] nomiColonne = {
                "Nome",
                "Cognome",
                "Telefono"
        };

        modelloTabella = new DefaultTableModel(nomiColonne, 0) {
            @Override
            public boolean isCellEditable(int riga, int colonna) {
                return false;
            }
        };

        tabella = new JTable(modelloTabella);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        caricaPersoneDaFile();

        JScrollPane pannelloTabella = new JScrollPane(tabella);

        JPanel pannelloSuperiore = new JPanel(new BorderLayout());

        pannelloSuperiore.add(titolo, BorderLayout.NORTH);
        pannelloSuperiore.add(creaBarraStrumenti(), BorderLayout.SOUTH);

        add(pannelloSuperiore, BorderLayout.NORTH);
        add(pannelloTabella,BorderLayout.CENTER);

    }

    private void caricaPersoneDaFile() {
        File cartella = new File(NOME_CARTELLA);

        if (!cartella.exists()) {
            return;
        }
        File[] filePersone = cartella.listFiles();

        if(filePersone==null){
            return;
        }

        for(File filePersona : filePersone) {
            try (Scanner scanner = new Scanner(filePersona)) {

                if (scanner.hasNextLine()) {
                    String riga = scanner.nextLine();

                    String[] dati = riga.split(";");
                    if (dati.length == 5) {
                        String nome = dati[0];
                        String cognome = dati[1];
                        String indirizzo = dati[2];
                        String telefono = dati[3];
                        int eta = Integer.parseInt(dati[4]);

                        Persona persona = new Persona(nome, cognome, indirizzo, telefono, eta);

                        aggiungiPersona(persona);
                    }
                }

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Errore durante la lettura del file.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private ImageIcon caricaIcona(String nomeFile) {
        java.net.URL percorso = getClass().getResource("/icons/" + nomeFile);

        if (percorso == null) {
            System.out.println("Icona non trovata: " + nomeFile);
            return new ImageIcon();
        }
        ImageIcon iconaOriginale= new ImageIcon(percorso);
        Image immagineRidimensionata = iconaOriginale.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);


        return new ImageIcon(immagineRidimensionata);
    }

    private JToolBar creaBarraStrumenti() {
        JToolBar barraStrumenti = new JToolBar();

        barraStrumenti.setFloatable(false);

        JButton nuovoToolbar = new JButton(
                "Nuovo",
                caricaIcona("nuovo.png")
        );

        JButton modificaToolbar = new JButton(
                "Modifica",
                caricaIcona("modifica.png")
        );

        JButton eliminaToolbar = new JButton(
                "Elimina",
                caricaIcona("elimina.png")
        );

        nuovoToolbar.addActionListener(e -> apriEditorNuovo());
        modificaToolbar.addActionListener(e -> apriEditorModifica());
        eliminaToolbar.addActionListener(e -> eliminaPersona());

        barraStrumenti.add(nuovoToolbar);
        barraStrumenti.add(modificaToolbar);
        barraStrumenti.add(eliminaToolbar);

        return barraStrumenti;
    }


    private void aggiungiPersona(Persona persona) {
        listaPersone.add(persona);
        modelloTabella.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getTelefono()});
    }

    private void aggiornaRiga(int riga, Persona persona) {
        listaPersone.set(riga, persona);

        modelloTabella.setValueAt(persona.getNome(), riga, 0);

        modelloTabella.setValueAt(persona.getCognome(), riga, 1);

        modelloTabella.setValueAt(persona.getTelefono(), riga, 2);
    }

    private void eliminaPersona(){
        int rigaSelezionata= tabella.getSelectedRow();

        if(rigaSelezionata == -1){
            JOptionPane.showMessageDialog(this,"Seleziona prima una persona da eliminare", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int risposta=JOptionPane.showConfirmDialog(this,"eliminare la persona selezionata?","Conferma eliminazione",JOptionPane.YES_NO_OPTION);

        if(risposta==JOptionPane.YES_OPTION){
            listaPersone.remove(rigaSelezionata);
            modelloTabella.removeRow(rigaSelezionata);
            salvaPersoneSuFile();
        }

    }

    private void apriEditorModifica() {
        int rigaSelezionata = tabella.getSelectedRow();

        if (rigaSelezionata == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona prima una persona da modificare.",
                    "Nessuna persona selezionata",
                    JOptionPane.ERROR_MESSAGE
            );
            System.out.println("Lista persone:"+listaPersone);
            return;
        }

        Persona personaSelezionata = listaPersone.get(rigaSelezionata);

        EditorPersona editor = new EditorPersona(personaSelezionata, personaSalvata -> {aggiornaRiga(rigaSelezionata, personaSalvata); salvaPersoneSuFile();});

        editor.setLocationRelativeTo(this);
        editor.setVisible(true);
    }

    private void apriEditorNuovo() {
        EditorPersona editor = new EditorPersona(personaSalvata -> { aggiungiPersona(personaSalvata); salvaPersoneSuFile(); });
        editor.setLocationRelativeTo(this);
        editor.setVisible(true);
    }

    private void salvaPersoneSuFile() {
        File cartella = ottieniCartellaInformazioni();
        cancellaFilePersone(cartella);

        try {
            for (int i = 0; i < listaPersone.size(); i++) {
                Persona persona = listaPersone.get(i);

                File filePersona = new File(
                        cartella,
                        "Persona" + (i + 1) + ".txt"
                );
                try (PrintStream output = new PrintStream(filePersona)) {
                    output.println(persona.getNome() + ";" + persona.getCognome() + ";" + persona.getIndirizzo() + ";" + persona.getTelefono() + ";" + persona.getEta());

                }
                }
            }catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore durante il salvataggio del file.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private File ottieniCartellaInformazioni() {
        File cartella = new File(NOME_CARTELLA);

        if (!cartella.exists()) {
            cartella.mkdir();
        }

        return cartella;
    }

    private void cancellaFilePersone(File cartella) {
        File[] filePersone = cartella.listFiles();

        if (filePersone == null) {
            return;
        }

        for (File file : filePersone) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                file.delete();
            }
        }
    }

}

