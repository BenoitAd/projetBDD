package BD;

/**
 * Classe d'erreur SQL
 */
public class ErreurSQL {

    /**
     * Code d'erreur
     */
    private int errorCode;

    /**
     * Message d'erreur
     */
    private String errorMessage;

    /**
     * Constructeur de la classe
     * @param errorCode
     * @param errorMessage
     */
    public ErreurSQL(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Setter du code erreur
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Setter du message erreur
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Getter du code erreur
     * @return errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Getter du message erreur
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Méthode toString
     * @return String
     */
    public String toString() {
        return "BD.ErreurSQL errorCode=" + errorCode + ", errorMessage=" + errorMessage ;
    }
}