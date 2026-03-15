package seedu.address.security;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.scene.control.TextInputDialog;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;

/**
 * Manages the security and authentication state of the application.
 * Manager handles password setup and basic file-based authentication checks.
 */
public class SecurityManager implements Security {
    private static final Logger logger = LogsCenter.getLogger(SecurityManager.class);
    private static final Path PASSWORD_FILE_PATH = Paths.get("data", "password.txt");

    /**
     * Checks if the user is authenticated.
     * If the password file exists, the user is considered authenticated.
     * Otherwise, triggers a setup dialog for the user to initialize a password.
     *
     * @return true if the password file exists or is successfully created, false otherwise.
     */
    @Override
    public boolean isAuthenticated() {
        if (FileUtil.isFileExists(PASSWORD_FILE_PATH)) {
            return true;
        }
        return showPasswordSetupDialog();
    }

    /**
     * Displays a JavaFX dialog to set up a new password and saves it to the local file system.
     *
     * @return true if the password was successfully entered and saved, false if the dialog was cancelled
     * or an error occurred during file writing.
     */
    private boolean showPasswordSetupDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Security Setup");
        dialog.setHeaderText("No password found.");
        dialog.setContentText("Enter your password:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                FileUtil.createParentDirsOfFile(PASSWORD_FILE_PATH);
                FileUtil.writeToFile(PASSWORD_FILE_PATH, result.get());
                return true;
            } catch (IOException e) {
                logger.severe("Could not save password file: " + e.getMessage());
            }
        }
        return false;
    }
}