package com.example.banque.utilisateur.out;

import com.example.banque.utilisateur.Utilisateur;
import com.example.banque.utilisateur.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UtilisateurCourantSecurity implements UtilisateurCourant {

    private final UtilisateurRepository utilisateurs;

    public UtilisateurCourantSecurity(UtilisateurRepository utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public Optional<Utilisateur> get() {
        Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
        if (authentification == null || !authentification.isAuthenticated()) {
            return Optional.empty();
        }
        String login;
        Object principal = authentification.getPrincipal();
        if (principal instanceof UserDetails details) {
            login = details.getUsername();
        } else if (principal instanceof String texte) {
            login = texte;
        } else {
            return Optional.empty();
        }
        return utilisateurs.findByLogin(login);
    }
}