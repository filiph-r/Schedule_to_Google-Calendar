package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Kalendar koj treba da primi sve podatke iz json fajla nakon parsiranja.
 * Pocetni i krajnji datum kao i neradne dane.
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class Kalendar {
	private String pocetniDatum;
	private String krajnjiDatum;
	private String[] neradniDani;

	public Date pocetak;
	public Date kraj;
	public ArrayList<Date> neradni;

	/**
	 * Parsira datume koji su iz jsonda dobijeni kao string u Date java java objekat
	 * 
	 * @throws ParseException
	 *             Ukoliko je doslo do greske prilikom parsiranja datuma. Moze se
	 *             desiti da nije prosledjen u pravom formatu, mada jsonMetasema bi
	 *             trebala da je to vec sprecila.
	 */
	public void parse() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		pocetak = df.parse(pocetniDatum);
		kraj = df.parse(krajnjiDatum);

		neradni = new ArrayList<>();
		for (int i = 0; i < neradniDani.length; i++) {
			neradni.add(df.parse(neradniDani[i]));
		}
	}

	public String getPocetniDatum() {
		return pocetniDatum;
	}

	public void setPocetniDatum(String pocetniDatum) {
		this.pocetniDatum = pocetniDatum;
	}

	public String getKrajnjiDatum() {
		return krajnjiDatum;
	}

	public void setKrajnjiDatum(String krajnjiDatum) {
		this.krajnjiDatum = krajnjiDatum;
	}

	public String[] getNeradniDani() {
		return neradniDani;
	}

	public void setNeradniDani(String[] neradniDani) {
		this.neradniDani = neradniDani;
	}

}
