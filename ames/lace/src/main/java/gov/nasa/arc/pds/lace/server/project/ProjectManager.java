package gov.nasa.arc.pds.lace.server.project;

import gov.nasa.arc.pds.lace.server.ServerConfiguration;
import gov.nasa.arc.pds.lace.shared.project.ProjectItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.DatatypeConverter;

/**
 * <p>Implements a project manager for user-specific project
 * areas for labels and uploaded schema files. All projects
 * and files are under a root directory configured in the
 * server configuration.</p>
 *
 * <p>Each user has a directory underneath the project area
 * root named with the URL-encoded, UTF-8 form of the user's
 * ID. Underneath the user's directory are two directories,
 * <code>default</code> and <projects>. The <code>default</code>
 * directory has the same structure as a project directory, as
 * described below.</p>
 *
 * <p><em>Project directories.</em> Project directories have
 * names that are generated by the project manager to be unique,
 * but otherwise have no significance. Project and label names
 * chosen by the user are recorded in a properties file
 * describing the project.</p>
 */
@Singleton
public class ProjectManager {

	/**
	 * The states that a user can be in with respect to their
	 * registration to use the site.
	 */
	public static enum UserRegistrationState {

		/** The user has not yet registered. */
		UNREGISTERED,

		/** The user has registered but not yet been approved. */
		AWAITING_APPROVAL,

		/** The user is approved to use the application. */
		APPROVED,

		/** The user registration request has been denied. */
		DENIED;

	}

	private static final String IGNORED_FILES_PATTERN_STR = "\\..*";
	private static final Pattern IGNORED_FILES_PATTERN = Pattern.compile(IGNORED_FILES_PATTERN_STR);

	private static final String USER_ROOT_PREFIX = "user";
	private static final String PROJECT_ITEM_PREFIX = "item";

	private static final String DEFAULT_LABEL_NAME = "Untitled.xml";

	private static final String USER_CONFIG_FILE = "user.properties";

	private static final String USER_REGISTRATION_PROPERTY = "user.registration-state";
	private static final String USER_ID_PROPERTY = "user.id";
	private static final String USER_PROPERTIES_PREFIX = "user.property.";

	private static final String LABEL_FILE_NAME = "label.xml";

	private static final String USER_DEFAULTS_DIRECTORY = "defaults";
	private static final String USER_PROJECTS_DIRECTORY = "projects";
	private static final String SCHEMA_FILE_DIRECTORY = "schema";

	/** A pattern string for an identifying string for a user or a location. */
	private static final String IDENT = "[A-Za-z0-9_-]+";

	/** The pattern for a location of a label or folder. The location can only consist
	 * of slash-separated identifiers.
	 */
	private static final Pattern LOCATION_PATTERN = Pattern.compile("(" + IDENT + "(/" + IDENT + ")*)?");

	private ServerConfiguration serverConfig;

	private Random random = new Random();

	/**
	 * Creates a new instance with a given server configuration.
	 * The server configuration is used to determine the root
	 * directory for all user files.
	 *
	 * @param config the server configuration
	 */
	@Inject
	public ProjectManager(ServerConfiguration config) {
		serverConfig = config;
	}

	/**
	 * Gets the project items for user at a given location. Project
	 * items are represented as subdirectories of the user's
	 * projects directory. Each item may be a label or a folder
	 * containing other folders or labels.
	 *
	 * @param userID a string identifying the user
	 * @param location the location within the user's projects area
	 * @return an array of project items
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ProjectItem[] getProjectItems(String userID, String location) throws FileNotFoundException, IOException {
		File itemDir = getItemFolder(userID, location);
		List<ProjectItem> result = new ArrayList<ProjectItem>();

		String locationPrefix = (location.isEmpty() ? "" : location+"/");

		for (File f : itemDir.listFiles()) {
			if (f.isDirectory() && f.canRead()) {
				try {
					ItemConfiguration itemConfig = new ItemConfiguration(f);
					ProjectItem newItem = new ProjectItem();
					newItem.setType(itemConfig.getItemType());
					newItem.setName(itemConfig.getItemName());
					newItem.setLastUpated(itemConfig.getLastUpdated());
					newItem.setLocation(locationPrefix + f.getName());
					result.add(newItem);
				} catch (FileNotFoundException e) {
					System.err.println("Ignoring nonexistent project item: " + f.toString());
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("Ignoring error reading project item: " + f.toString());
					e.printStackTrace();
				}
			}
		}

		ProjectItem[] items = result.toArray(new ProjectItem[result.size()]);
		Arrays.sort(items);
		return items;
	}

	// Default scope, for user testing.
	File getItemFolder(String userID, String location) throws FileNotFoundException, IOException {
		Matcher matcher = LOCATION_PATTERN.matcher(location);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Location does not match required pattern: " + location);
		}

		return new File(getUserItemRoot(userID), location.replaceAll("/", File.separator));
	}

	// Default scope, for unit testing.
	File getUserRoot(String userID) throws FileNotFoundException, IOException {
		// Try to find the right user root dir.
		for (File child : serverConfig.getProjectRoot().listFiles()) {
			String id = getUserID(child);
			// Ignore case, since we're using email addresses.
			if (userID.equalsIgnoreCase(id)) {
				return child;
			}
		}

		return null;
	}

	File getUserItemRoot(String userID) throws FileNotFoundException, IOException {
		return new File(getUserRoot(userID), USER_PROJECTS_DIRECTORY);
	}

	private Properties getUserConfiguration(File userRootDir) throws FileNotFoundException, IOException {
		Properties props = new Properties();

		File userConfigFile = new File(userRootDir, USER_CONFIG_FILE);
		if (userConfigFile.exists() && userConfigFile.isFile() && userConfigFile.canRead()) {
			props.load(new FileInputStream(userConfigFile));
		}

		return props;
	}

	private void writeUserConfiguration(File userRootDir, Properties props) throws FileNotFoundException, IOException {
		File userConfigFile = new File(userRootDir, USER_CONFIG_FILE);
		props.store(new FileOutputStream(userConfigFile), "User updated at " + (new Date()).toString());
		userConfigFile.setWritable(true, false);
	}

	private String getUserID(File userRootDir) throws FileNotFoundException, IOException {
		Properties props = getUserConfiguration(userRootDir);
		return props.getProperty(USER_ID_PROPERTY);
	}

	/**
	 * Tests whether a user is authorized to use the application. A user is authorized
	 * by setting the "authorized" property to true in the user configuration file.
	 *
	 * @param userID the user ID
	 * @return true, if the user is authorized to use the application, false otherwise
	 * @throws FileNotFoundException if the configuration file cannot be found for the user
	 * @throws IOException if there is an error reading the user configuration
	 */
	public boolean isUserAuthorized(String userID) throws FileNotFoundException, IOException {
		UserRegistrationState state = getUserRegistrationState(userID);
		return state == UserRegistrationState.APPROVED;
	}

	/**
	 * Gets the registration state of a user.
	 *
	 * @param userID the user ID
	 * @return the registration state of the user
	 * @throws FileNotFoundException if the configuration file cannot be found for the user
	 * @throws IOException if there is an error reading the user configuration
	 */
	public UserRegistrationState getUserRegistrationState(String userID) throws FileNotFoundException, IOException {
		File userRoot = getUserRoot(userID);
		if (userRoot == null) {
			return UserRegistrationState.UNREGISTERED;
		}

		Properties props = getUserConfiguration(userRoot);
		if (!props.containsKey(USER_REGISTRATION_PROPERTY)) {
			return UserRegistrationState.AWAITING_APPROVAL;
		}

		UserRegistrationState state = UserRegistrationState.valueOf(props.getProperty(USER_REGISTRATION_PROPERTY).toUpperCase());
		return state;
	}

	/**
	 * Sets the registration state of a user.
	 *
	 * @param userID the user ID
	 * @param newState the new registration state
	 * @throws FileNotFoundException if the configuration file for the user cannot be found
	 * @throws IOException if there is an exception writing the user configuration
	 */
	public void setUserRegistrationState(String userID, UserRegistrationState newState) throws FileNotFoundException, IOException {
		createUserIfNeccesary(userID);

		File userRootDir = getUserRoot(userID);
		Properties props = getUserConfiguration(userRootDir);
		props.setProperty(USER_REGISTRATION_PROPERTY, newState.toString());
		writeUserConfiguration(userRootDir, props);
	}

	/**
	 * Creates a projects area for a user, if it doesn't already exist.
	 *
	 * @param userID the user ID
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void createUserIfNeccesary(String userID) throws FileNotFoundException, IOException {
		File root = getUserRoot(userID);
		if (root == null) {
			root = createNewUserRoot();
			createDirectory(root);
			createDirectory(new File(root, USER_PROJECTS_DIRECTORY));
			createDirectory(new File(root, USER_DEFAULTS_DIRECTORY));

			File userConfigFile = new File(root, USER_CONFIG_FILE);
			Properties props = new Properties();
			props.setProperty(USER_ID_PROPERTY, userID);
			props.setProperty(USER_REGISTRATION_PROPERTY, UserRegistrationState.UNREGISTERED.toString());
			props.store(new FileOutputStream(userConfigFile), "User created at " + (new Date()).toString());
			userConfigFile.setWritable(true, false);
		}
	}

	private void createDirectory(File d) {
		d.mkdirs();
		d.setWritable(true, false);
	}

	private File createNewUserRoot() {
		for (int i=0; i < 100; ++i) {
			File userRoot = new File(serverConfig.getProjectRoot(), USER_ROOT_PREFIX + random.nextInt());
			if (!userRoot.exists()) {
				return userRoot;
			}
		}

		// If we get here, failed to create a root directory.
		throw new IllegalStateException("Failed to create a new user directory");
	}

	/**
	 * Gets the file for a label project item at a given location.
	 *
	 * @param userID the user ID
	 * @param location the item location
	 * @return a file representing the label file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public File getLabelFile(String userID, String location) throws FileNotFoundException, IOException {
		File folder = getItemFolder(userID, location);
		return new File(folder, LABEL_FILE_NAME);
	}

	/**
	 * Gets the default schema files for creating new labels. All files
	 * in the default schema directory are used except those matching
	 * a pattern for names of ignored files.
	 *
	 * @param userID the user ID
	 * @return an array of schema files
	 * @throws IOException if there is an error reading the user configuration
	 */
	public File[] getDefaultSchemaFiles(String userID) throws IOException {
		File root = getUserRoot(userID);
		File defaultsDir = new File(root, USER_DEFAULTS_DIRECTORY);
		File defaultSchemaDir = new File(defaultsDir, SCHEMA_FILE_DIRECTORY);
		if (!defaultSchemaDir.exists() || !defaultSchemaDir.isDirectory()) {
			return new File[0];
		} else {
			return defaultSchemaDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File parent, String name) {
					Matcher matcher = IGNORED_FILES_PATTERN.matcher(name);
					return !matcher.matches();
				}
			});
		}
	}

	/**
	 * Gets the schema files for a project item.
	 *
	 * @param userID the user ID
	 * @param location the item location
	 * @return an array of schema files
	 * @throws IOException if there is an error accessing the schema files
	 */
	public File[] getItemSchemaFiles(String userID, String location) throws IOException {
		File itemDir = getItemFolder(userID, location);
		File schemaDir = new File(itemDir, SCHEMA_FILE_DIRECTORY);
		if (!schemaDir.exists() || !schemaDir.isDirectory()) {
			return new File[0];
		} else {
			return schemaDir.listFiles();
		}
	}

	/**
	 * Adds a file to the default schema files for creating or importing labels.
	 * Copies the schema file to the proper location.
	 *
	 * @param userID the user ID
	 * @param f the new schema file
	 * @throws IOException if there is an error reading the user configuration or copying the file
	 */
	public void addDefaultSchemaFile(String userID, File f) throws IOException {
		File root = getUserRoot(userID);
		File defaultsDir = new File(root, USER_DEFAULTS_DIRECTORY);
		File defaultSchemaDir = new File(defaultsDir, SCHEMA_FILE_DIRECTORY);

		createDirectory(defaultSchemaDir);

		File destFile = new File(defaultSchemaDir, f.getName());
		copyFile(f, destFile);
	}

	private void copyFile(File fromFile, File toFile) throws IOException {
		byte[] buf = new byte[8192];
		InputStream in = new FileInputStream(fromFile);
		OutputStream out = new FileOutputStream(toFile);
		toFile.setWritable(true, false);

		try {
			int nRead;
			while ((nRead = in.read(buf)) > 0) {
				out.write(buf, 0, nRead);
			}
		} finally {
			in.close();
			out.close();
		}
	}

	/**
	 * Creates a new label and gets the new project location.
	 *
	 * @param the user ID
	 * @param location the parent location
	 * @return the project location
	 * @throws IOException if there is an error getting the parent item folder
	 */
	public String createNewLabel(String userID, String location) throws IOException {
		File parent = getItemFolder(userID, location);
		File labelDir = createNewLabel(parent);
		copyDefaultSchemaFiles(userID, new File(labelDir, SCHEMA_FILE_DIRECTORY));
		return (location.isEmpty() ? labelDir.getName() : location+"/"+labelDir.getName());
	}

	private File createNewLabel(File parent) throws IOException {
		for (int i=0; i < 100; ++i) {
			File itemDir = new File(parent, PROJECT_ITEM_PREFIX + random.nextInt());
			if (!itemDir.exists()) {
				createLabelInfo(itemDir);
				return itemDir;
			}
		}

		// If we get here, failed to create a root directory.
		throw new IllegalStateException("Failed to create a new user directory");
	}

	private void createLabelInfo(File labelDir) throws IOException {
		createDirectory(labelDir);

		ItemConfiguration config = new ItemConfiguration(labelDir, true);
		config.setItemType(ProjectItem.Type.LABEL);
		config.setItemName(DEFAULT_LABEL_NAME);
		config.saveConfiguration();
	}

	private void copyDefaultSchemaFiles(String userID, File schemaFileDir) throws IOException {
		createDirectory(schemaFileDir);

		for (File f : getDefaultSchemaFiles(userID)) {
			File schemaFile = new File(schemaFileDir, f.getName());
			copyFile(f, schemaFile);
		}
	}

	/**
	 * Copies schema files from a project item into the defaults.
	 *
	 * @param userID the user ID
	 * @param location the item location
	 */
	public void copyDefaults(String userID, String location) throws IOException {
		removeDefaults(userID);

		File root = getUserRoot(userID);
		File defaultsDir = new File(root, USER_DEFAULTS_DIRECTORY);
		File defaultSchemaDir = new File(defaultsDir, SCHEMA_FILE_DIRECTORY);
		for (File f : getItemSchemaFiles(userID, location)) {
			File newDefault = new File(defaultSchemaDir, f.getName());
			copyFile(f, newDefault);
		}
	}

	private void removeDefaults(String userID) throws IOException {
		for (File f : getDefaultSchemaFiles(userID)) {
			f.delete();
		}
	}

	/**
	 * Sets the name of a project item.
	 *
	 * @param userID the user ID
	 * @param location the project item location
	 * @param newName the new item name
	 * @throws IOException if there is an error reading or writing the item configuration
	 */
	public void setProjectItemName(String userID, String location, String newName) throws IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		config.setItemName(newName);
		config.saveConfiguration();
	}

	/**
	 * Gets a project item attribute.
	 *
	 * @param userID the user ID
	 * @param location the project item location
	 * @param key the attribute key
	 * @return the attribute value
	 * @throws IOException if there is an error reading the item configuration
	 */
	public String getProjectItemAttribute(String userID, String location, String key) throws IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		return config.getItemAttribute(key);
	}

	/**
	 * Sets the value of a project item attribute.
	 *
	 * @param userID the user ID
	 * @param location the project item location
	 * @param key the attribute key
	 * @param value the new attribute value
	 * @throws IOException if there is an error reading or writing the item configuration
	 */
	public void setProjectItemAttribute(String userID, String location, String key, String value) throws IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		config.setItemAttribute(key, value);
		config.saveConfiguration();
	}

	/**
	 * Removes a project item attribute.
	 *
	 * @param userID the user ID
	 * @param location the project item location
	 * @param key the attribute key
	 * @throws IOException if there is an error reading the item configuration
	 */
	public void removeProjectItemAttribute(String userID, String location, String key) throws IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		config.removeItemAttribute(key);
		config.saveConfiguration();
	}

	/**
	 * Deletes a local schema file for a user.
	 *
	 * @param userID the user ID
	 * @param filename the local schema file name
	 * @throws IOException if there is an error reading the user configuration or deleting the file
	 */
	public void removeDefaultSchemaFile(String userID, String filename) throws IOException {
		for (File f : getDefaultSchemaFiles(userID)) {
			if (f.getName().equals(filename)) {
				f.delete();
			}
		}
	}

	/**
	 * Deletes a project item for a user.
	 *
	 * @param user the user ID
	 * @param location the item location
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void deleteProjectItem(String user, String location) throws FileNotFoundException, IOException {
		File folder = getItemFolder(user, location);
		deleteRecursively(folder);
	}

	private void deleteRecursively(final File f) {
		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				deleteRecursively(child);
			}
		}

		f.delete();
	}

	/**
	 * Sets the last updated date for a label.
	 *
	 * @param userID the user ID
	 * @param location the item location
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void setLabelLastUpdated(String userID, String location, Date date) throws FileNotFoundException, IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		config.setLastUpdated(date);
		config.saveConfiguration();
	}

	/**
	 * Sets the last updated date for a label, if the label has changed.
	 * A message digest is used to determine if the label has changed.
	 *
	 * @param userID the user ID
	 * @param location the label location
	 * @param date the new date
	 * @param digest the new message digest value
	 */
	public void setLabelLastUpdatedIfNecessary(String userID, String location, Date date, byte[] digest) throws FileNotFoundException, IOException {
		File itemRoot = getItemFolder(userID, location);
		ItemConfiguration config = new ItemConfiguration(itemRoot);
		String newDigestStr = DatatypeConverter.printBase64Binary(digest);
		String oldDigestStr = config.getDigest();

		if (oldDigestStr==null || !oldDigestStr.equals(newDigestStr)) {
			config.setDigest(newDigestStr);
			config.setLastUpdated(date);
			config.saveConfiguration();
		}
	}

	/**
	 * Gets the properties for a user.
	 *
	 * @param userID the user ID
	 * @return a map containing properties for the user, or null if no such user
	 * @throws FileNotFoundException if the user directory cannot be found
	 * @throws IOException if there is an error reading the properties
	 */
	public Map<String, String> getUserProperties(String userID) throws FileNotFoundException, IOException {
		File userRoot = getUserRoot(userID);
		if (userRoot == null) {
			return null;
		}

		Map<String, String> userProperties = new HashMap<String, String>();

		Properties props = getUserConfiguration(userRoot);
		for (Object o : props.keySet()) {
			String key = (String) o;
			if (key.startsWith(USER_PROPERTIES_PREFIX)) {
				String name = key.substring(USER_PROPERTIES_PREFIX.length());
				String value = (String) props.get(key);
				userProperties.put(name, value);
			}
		}

		return userProperties;
	}

	/**
	 * Sets the properties associated with a user.
	 *
	 * @param userID the user ID
	 * @param userProperties a map of user properties
	 * @throws FileNotFoundException if the user directory cannot be found
	 * @throws IOException if there is an error writing the properties
	 */
	public void setUserProperties(String userID, Map<String, String> userProperties) throws FileNotFoundException, IOException {
		File userRoot = getUserRoot(userID);
		if (userRoot == null) {
			throw new IllegalArgumentException("Unknown user id (" + userID + ")");
		}

		Properties props = getUserConfiguration(userRoot);
		for (Entry<String, String> entry : userProperties.entrySet()) {
			props.setProperty(USER_PROPERTIES_PREFIX + entry.getKey(), entry.getValue());
		}

		writeUserConfiguration(userRoot, props);
	}

}
