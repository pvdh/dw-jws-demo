package de.vonderhagen.dw.core;

import java.security.Principal;
import java.util.List;

public class User implements Principal {

    private String subject;
    private String mail;
    private List<String> eduPersonScopedAffiliation;

    public User(String subject) {
        this.subject = subject;
    }

    @Override
    public String getName() {
        return getSubject();
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return the eduPersonScopedAffiliation
     */
    public List<String> getEduPersonScopedAffiliation() {
        return eduPersonScopedAffiliation;
    }

    /**
     * @param eduPersonScopedAffiliation the eduPersonScopedAffiliation to set
     */
    public void setEduPersonScopedAffiliation(List<String> eduPersonScopedAffiliation) {
        this.eduPersonScopedAffiliation = eduPersonScopedAffiliation;
    }

}
