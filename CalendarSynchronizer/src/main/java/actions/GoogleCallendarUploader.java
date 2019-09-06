package actions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import models.Kalendar;
import models.Raspored;
import models.Termin;

/**
 * vecina je preuzeta sa od googlovog api-a. Sluzi za sinhronizaciju kalendara
 * prosledjenog kroz objekat u google kalendar
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class GoogleCallendarUploader {

	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File("credentials/calendar-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/calendar-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GoogleCallendarUploader.class.getResourceAsStream("/client_id.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Calendar client service.
	 * 
	 * @return an authorized Calendar client service
	 * @throws IOException
	 */
	public static Calendar getCalendarService() throws IOException {
		Credential credential = authorize();
		return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	/**
	 * Povezuje se sa korisnickim Google nalogom i sinhronizuje kalnedar sa podacima
	 * iz rasporeda u intervalu koj prosledjeni kalendar zadaje
	 * 
	 * @param kalendar
	 *            kalendar za koji važi dati raspored
	 * @param raspored
	 *            nedeljni raspored
	 */
	public static void upload(Kalendar kalendar, Raspored raspored) {
		File file = new File(DATA_STORE_DIR.getAbsolutePath() + "\\StoredCredential");
		file.delete();

		try {
			ArrayList<Event> events = new ArrayList<>();

			long days = kalendar.kraj.getTime() - kalendar.pocetak.getTime();
			days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);

			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm");
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(kalendar.pocetak);
			for (int i = 0; i < days; i++) {
				if (!kalendar.neradni.contains(cal.getTime())) {
					for (Termin t : raspored.getTermini()) {
						if (proveriDan(t, cal.getTime())) {
							Event event = new Event();
							event.setSummary(t.getPredmet());
							event.setLocation(t.getUcionica());
							event.setDescription(t.getNastavnik() + "\n" + t.getTip() + "\n" + t.getGrupe());

							String pocetak = t.getVreme().split("-")[0];
							String kraj = t.getVreme().split("-")[1] + ":00";
							String datum = formater.format(cal.getTime());

							Date date = df.parse(datum + " at " + pocetak);
							DateTime startDateTime = new DateTime(date);
							EventDateTime start = new EventDateTime().setDateTime(startDateTime);
							event.setStart(start);

							date = df.parse(datum + " at " + kraj);
							DateTime endDateTime = new DateTime(date);
							EventDateTime end = new EventDateTime().setDateTime(endDateTime);
							event.setEnd(end);

							events.add(event);
						}
					}
				}

				// uvecavamo za jedan dan
				cal.add(java.util.Calendar.DATE, 1);
			}

			if (!events.isEmpty())
				uploadEvents(events);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Konkretno ovde citavu listu eventova stavlja na google calendar
	 * 
	 * @param events
	 *            lista eventova koji su ucitani
	 * @throws Exception
	 *             Ukoliko neki event nije mogo da se uploaduje na google
	 */
	public static void uploadEvents(ArrayList<Event> events) throws Exception {
		Calendar service = getCalendarService();

		for (Event event : events) {
			service.events().insert("primary", event).execute();
		}
	}

	/**
	 * Proverava dali se slazu dani u datumu i terminu
	 * 
	 * @param termin
	 *            Termin iz rasporeda
	 * @param date
	 *            datum u opsegu koj je dat kroz kalendar
	 * @return
	 */
	private static boolean proveriDan(Termin termin, Date date) {
		String weekDay = date.toString().substring(0, 3);
		if (termin.getDan().equals("PON") && weekDay.equals("Mon"))
			return true;
		else if (termin.getDan().equals("UTO") && weekDay.equals("Tue"))
			return true;
		else if (termin.getDan().equals("SRE") && weekDay.equals("Wed"))
			return true;
		else if (termin.getDan().equals("CET") && weekDay.equals("Thu"))
			return true;
		else if (termin.getDan().equals("PET") && weekDay.equals("Fri"))
			return true;
		else if (termin.getDan().equals("SUB") && weekDay.equals("Sat"))
			return true;
		else if (termin.getDan().equals("NED") && weekDay.equals("Sun"))
			return true;

		return false;
	}

}
