package models;

/**
 * Nedeljni termin sa svim podacima koji se mogu naci u raf-ovom rasporedu
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class Termin {

	private String predmet;
	private String tip;
	private String nastavnik;
	private String grupe;
	private String dan;
	private String vreme;
	private String ucionica;

	public String getPredmet() {
		return predmet;
	}

	public void setPredmet(String predmet) {
		this.predmet = predmet;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(String nastavnik) {
		this.nastavnik = nastavnik;
	}

	public String getGrupe() {
		return grupe;
	}

	public void setGrupe(String grupe) {
		this.grupe = grupe;
	}

	public String getDan() {
		return dan;
	}

	public void setDan(String dan) {
		this.dan = dan;
	}

	public String getVreme() {
		return vreme;
	}

	public void setVreme(String vreme) {
		this.vreme = vreme;
	}

	public String getUcionica() {
		return ucionica;
	}

	public void setUcionica(String ucionica) {
		this.ucionica = ucionica;
	}

	@Override
	public String toString() {
		return predmet + "\n" + tip + "\n" + nastavnik + "\n" + grupe + "\n" + dan + "\n" + vreme + "\n" + ucionica;
	}

}
