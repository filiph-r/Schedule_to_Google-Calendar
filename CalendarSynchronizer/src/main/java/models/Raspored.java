package models;

/**
 * Raspored koj drzi u sebi niz nedeljnih termina
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class Raspored {

	private Termin[] termini;

	public Termin[] getTermini() {
		return termini;
	}

	public void setTermini(Termin[] termini) {
		this.termini = termini;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < termini.length; i++) {
			str += termini[i].toString() + "\n\n";
		}

		return str;
	}

}
