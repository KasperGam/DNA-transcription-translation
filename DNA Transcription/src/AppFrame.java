import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class AppFrame extends JFrame implements ActionListener {

	private JTextField input;
	private JLabel inputLabel;
	private JTextArea ansArea;
	private JPanel inputPanel;
	private JPanel outputPanel;
	private JCheckBox templet;
	private JLabel templetLabel;
	private JPanel mrnaPanel;
	private JTextField mrnaField;
	private JLabel mrnaLabel;
	private JPanel northPanel;
	
	private JRadioButton longName;
	private JRadioButton medName;
	private JRadioButton shortName;
	private ButtonGroup nameGroup;
	
	private boolean dnaLast = true;
	
	public AppFrame(){
		setBounds(100, 100, 500, 500);
		setTitle("Transcription and Translation");
		setBackground(Color.WHITE);
		inputPanel = new JPanel();
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(2, 0));
		northPanel.setBackground(Color.GRAY);
		add(northPanel, BorderLayout.NORTH);
		northPanel.add(inputPanel);
		outputPanel = new JPanel();
		outputPanel.setBackground(Color.GRAY);
		add(outputPanel, BorderLayout.SOUTH);
		setUpInput();
		setUpOutput();
		setVisible(true);
	}
	
	private void setUpInput(){
		inputLabel = new JLabel("DNA: ");
		input = new JTextField(10);
		input.setText("DNA Sequence");
		templetLabel = new JLabel("Template Strand:");
		templet = new JCheckBox();
		templet.setSelected(true);
		inputPanel.add(inputLabel, BorderLayout.NORTH);
		inputPanel.add(input, BorderLayout.NORTH);
		inputPanel.add(templetLabel, BorderLayout.NORTH);
		inputPanel.add(templet, BorderLayout.NORTH);
		input.addActionListener(this);
		
		mrnaField = new JTextField(10);
		mrnaField.setText("mRNA sequence");
		mrnaLabel = new JLabel("mRNA: ");
		mrnaPanel = new JPanel();
		mrnaPanel.add(mrnaLabel);
		mrnaPanel.add(mrnaField);
		northPanel.add(mrnaPanel);
		mrnaField.addActionListener(this);
		
		longName = new JRadioButton("Whole name");
		medName = new JRadioButton("Abriviation");
		shortName = new JRadioButton("1 Letter");
		medName.setSelected(true);
		nameGroup = new ButtonGroup();
		nameGroup.add(longName);
		nameGroup.add(medName);
		nameGroup.add(shortName);
		mrnaPanel.add(longName);
		mrnaPanel.add(medName);
		mrnaPanel.add(shortName);
		
		longName.addActionListener(this);
		medName.addActionListener(this);
		shortName.addActionListener(this);
		
		inputPanel.setBackground(Color.GRAY);
		mrnaPanel.setBackground(Color.GRAY);
	}
	
	private void setUpOutput(){
		ansArea = new JTextArea(8, 20);
		ansArea.setText("Amino acid chain");
		outputPanel.add(ansArea);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String DNA = "";
		String mrna = "";
		boolean isValid = true;
		if (arg0.getSource()==input || (arg0.getSource().getClass().equals(JRadioButton.class)&& dnaLast)){
			dnaLast = true;
			DNA = input.getText();
			DNA = DNA.toUpperCase();
			DNA = DNA.replaceAll(" ", "");
			if(isDNA(DNA)){
				if(templet.isSelected()){
					for(int i=0; i<DNA.length(); i++){
						char current = DNA.charAt(i);
						char next = current;
						if(current=='T'){
							next = 'A';
						} else if (current=='A'){
							next = 'U';
						} else if (current=='G'){
							next = 'C';
						} else{
							next = 'G';
						}
						mrna+=next;
					}
				} else {
					mrna = DNA.replace('T', 'U');
				}
		
				mrnaField.setText(mrna);
			} else {
				ansArea.setText("Error in DNA sequence.");
				isValid = false;
			}
		} else if (arg0.getSource()==mrnaField ||arg0.getSource().getClass().equals(JRadioButton.class)){
			dnaLast = false;
			mrna = mrnaField.getText();
			mrna = mrna.toUpperCase();
			mrna = mrna.replaceAll(" ", "");
			if(ismRNA(mrna)){
				if(templet.isSelected()){
					for(int i=0; i<mrna.length(); i++){
						char current = mrna.charAt(i);
						char next = 'A';
						if(current=='U'){
							next = 'A';
						} else if (current=='A'){
							next = 'T';
						} else if (current=='G'){
							next = 'C';
						} else{
							next = 'G';
						}
						DNA+=next;
					}
				} else {
					DNA = mrna.replace('U', 'T');
				}
			
			input.setText(DNA);
			} else {
				ansArea.setText("Error in mRNA sequence.");
				isValid = false;
			}
		}
		
		//If there were no errors
		if(isValid){
			String ans = "";
			
			//first reading frame
			for(int i=0; i<=mrna.length()-3; i+=3){
				String codon = mrna.substring(i, i+3);
				try{
					String aminoAcid = getAminoAcid(codon);
					ans+=aminoAcid+" ";
				} catch (Exception e){
					System.out.println("Improper Codon!");
				}
			}
			ans+="\n\n";
			
			//second reading frame
			for(int i=1; i<=mrna.length()-3; i+=3){
				String codon = mrna.substring(i, i+3);
				try{
					String aminoAcid = getAminoAcid(codon);
					ans+=aminoAcid+" ";
				} catch (Exception e){
					System.out.println("Improper Codon!");
				}
			}
			ans+="\n\n";
			
			//third reading frame
			for(int i=2; i<=mrna.length()-3; i+=3){
				String codon = mrna.substring(i, i+3);
				try{
					String aminoAcid = getAminoAcid(codon);
					ans+=aminoAcid+" ";
				} catch (Exception e){
					System.out.println("Improper Codon!");
				}
			}
			ansArea.setText(ans);
		}
	}
	
	private boolean ismRNA(String messRNA){
		messRNA = messRNA.toUpperCase();
		for(int i=0; i<messRNA.length(); i++){
			char base = messRNA.charAt(i);
			if(base!='A' && base!='U' && base!='G' && base!='C'){
				return false;
			}
		}
		return true;
	}
	
	private boolean isDNA(String dna){
		dna = dna.toUpperCase();
		for(int i=0; i<dna.length(); i++){
			char base = dna.charAt(i);
			if(base!='A' && base!='T' && base!='G' && base!='C'){
				return false;
			}
		}
		return true;
	}
	
	private String getAminoAcid(String cdon) throws Exception{
		if(cdon.length()!=3){
			throw new Exception();
		}
		char first = cdon.charAt(0);
		char second = cdon.charAt(1);
		char third = cdon.charAt(2);
		
		if(first=='U'){
			if(second=='U'){
				if (third=='U'||third=='C'){
					if(longName.isSelected()){
						return "Phenylalanine";
					} else if (medName.isSelected()){
						return "PHE";
					} else {
						return "F";
					}
				} else {
					if(longName.isSelected()){
						return "Lecine";
					} else if (medName.isSelected()){
						return "LEU";
					} else {
						return "L";
					}
				}
			} else if (second=='C'){
				if(longName.isSelected()){
					return "Serine";
				} else if (medName.isSelected()){
					return "SER";
				} else {
					return "S";
				}
			} else if (second=='A'){
				if(third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Tyrosine";
					} else if (medName.isSelected()){
						return "TYR";
					} else {
						return "Y";
					}
				} else {
					if(longName.isSelected()){
						return "Selenocysteine/Pyrrolysine";
					} else if (medName.isSelected()){
						return "STOP";
					} else {
						return "U/O";
					}
				}
			} else {
				if(third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Cysteine";
					} else if (medName.isSelected()){
						return "CYS";
					} else {
						return "C";
					}
				} else if (third=='A'){
					if(longName.isSelected()){
						return "Selenocysteine/Pyrrolysine";
					} else if (medName.isSelected()){
						return "STOP";
					} else {
						return "U/O";
					}
				} else{
					if(longName.isSelected()){
						return "Tryptophan";
					} else if (medName.isSelected()){
						return "TRP";
					} else {
						return "W";
					}
				}
			}
		} else if (first=='C'){
			if (second=='U'){
				if(longName.isSelected()){
					return "Leucine";
				} else if (medName.isSelected()){
					return "LEU";
				} else {
					return "L";
				}
			} else if (second=='C'){
				if(longName.isSelected()){
					return "Proline";
				} else if (medName.isSelected()){
					return "PRO";
				} else {
					return "P";
				}
			} else if (second=='A'){
				if (third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Histidine";
					} else if (medName.isSelected()){
						return "HIS";
					} else {
						return "H";
					}
				} else{
					if(longName.isSelected()){
						return "Glutamine";
					} else if (medName.isSelected()){
						return "GLN";
					} else {
						return "Q";
					}
				}
			} else {
				if(longName.isSelected()){
					return "Arginine";
				} else if (medName.isSelected()){
					return "ARG";
				} else {
					return "R";
				}
			}
		} else if (first=='A'){
			if (second=='U'){
				if (third=='G'){
					if(longName.isSelected()){
						return "Methionine";
					} else if (medName.isSelected()){
						return "MET";
					} else {
						return "M";
					}
				} else {
					if(longName.isSelected()){
						return "Isoleucine";
					} else if (medName.isSelected()){
						return "ILE";
					} else {
						return "I";
					}
				}
			} else if (second=='C'){
				if(longName.isSelected()){
					return "Threonine";
				} else if (medName.isSelected()){
					return "THR";
				} else {
					return "T";
				}
			} else if (second=='A'){
				if (third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Asparagine";
					} else if (medName.isSelected()){
						return "ASN";
					} else {
						return "N";
					}
				} else {
					if(longName.isSelected()){
						return "Lysine";
					} else if (medName.isSelected()){
						return "LYS";
					} else {
						return "K";
					}
				}
			} else {
				if (third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Serine";
					} else if (medName.isSelected()){
						return "SER";
					} else {
						return "S";
					}
				} else {
					if(longName.isSelected()){
						return "Arginine";
					} else if (medName.isSelected()){
						return "ARG";
					} else {
						return "R";
					}
				}
			}
		} else {
			if (second=='U'){
				if(longName.isSelected()){
					return "Valine";
				} else if (medName.isSelected()){
					return "VAL";
				} else {
					return "V";
				}
			} else if (second=='C'){
				if(longName.isSelected()){
					return "Alanine";
				} else if (medName.isSelected()){
					return "ALA";
				} else {
					return "A";
				}
			} else if (second=='A'){
				if (third=='U' || third=='C'){
					if(longName.isSelected()){
						return "Aspartic acid";
					} else if (medName.isSelected()){
						return "ASP";
					} else {
						return "D";
					}
				} else {
					if(longName.isSelected()){
						return "Glutamic acid";
					} else if (medName.isSelected()){
						return "GLU";
					} else {
						return "E";
					}
				}
			} else {
				if(longName.isSelected()){
					return "Glycine";
				} else if (medName.isSelected()){
					return "GLY";
				} else {
					return "G";
				}
			}
		}
	}
}