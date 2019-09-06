package app;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonParser biblioteka sluzi za parsiranje Json fajlova u java object file po
 * datoj Json-Metasemi uz njegovu validaciju. <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 * 
 * @version 1.0
 *
 */

public class JsonParser {

	/**
	 * Json metasema za proveru validacije Json fajla
	 */
	private Schema metaSchema;

	/**
	 * Konstruktor za Json Parser. Potrebno je da mu se dodeli metasema po kojoj ce
	 * da parsira i vrsi validaciju.
	 * 
	 * @param jsonMetaschemaFile
	 *            Putanja ka Json fajlu Metaseme po kojoj ce parser da vrsi
	 *            validaciju i parsiranje
	 * @throws Exception
	 *             Baca exception ukoliko je doslo do greske prilikom ucitavanja i
	 *             setovanja metascheme
	 */
	public JsonParser(String jsonMetaschemaFile) throws Exception {
		setMetaschema(jsonMetaschemaFile);
	}

	/**
	 * Sluzi za prsiranje Json fajla
	 * 
	 * @param jsonFile
	 *            Putanja ka Json fajlu koj treba da se parsira
	 * @param javaClass
	 *            Java klasa po kojoj ce podaci iz Json fajla da budu uneseni
	 * @return Vraca Object koj je popunjen sa parametrima iz Json fajla
	 * @throws Exception
	 *             Ukoliko je doslo do greske prilikom ucitavanja Json Fajla,
	 *             njegove validacije ili ukoliko nemoze da se mapira po datoj klasi
	 */
	public Object parseJson(String jsonFile, Class javaClass) throws Exception {
		metaValidation(jsonFile);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

		return mapper.readValue(new File(jsonFile), javaClass);
	}

	/**
	 * Vrsi validaciju datog json Fajla po metasemi parsera
	 * 
	 * @param jsonFile
	 *            Putanja ka Json fajlu
	 * @throws Exception
	 *             Ukoliko je nastala greska prilikom citanja json fajla ili se ne
	 *             slaze sa metasemom
	 */
	public void metaValidation(String jsonFile) throws Exception {

		JSONObject schemaJson = loadJsonFromFile(jsonFile);

		try {
			metaSchema.validate(schemaJson);
		} catch (Exception e) {
			throw new Exception("Json NOT compatible with metaschema: " + e.getMessage());
		}
	}

	public void setMetaschema(String jsonMetaschemaFile) throws Exception {
		JSONObject metaschemaJson = loadJsonFromFile(jsonMetaschemaFile);
		metaSchema = SchemaLoader.load(metaschemaJson);
	}

	/**
	 * Kreira Json objekta iz datog fajla
	 * 
	 * @param fileName
	 *            Putanja do json fajla
	 * @return vraca JSON objekat
	 * @throws Exception
	 *             Ukoliko je nastala greska prilikom citanja json fajla ili dati
	 *             fajl nije korektan json format
	 */
	public JSONObject loadJsonFromFile(String fileName) throws Exception {

		try {

			Reader reader = new FileReader(fileName);
			return new JSONObject(new JSONTokener(reader));

		} catch (Exception e) {
			throw new Exception("Could not load " + fileName + " as JsonObject: " + e.getMessage());
		}
	}

}
