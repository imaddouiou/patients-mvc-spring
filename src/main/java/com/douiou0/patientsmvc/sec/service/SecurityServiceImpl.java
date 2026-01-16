package com.douiou0.patientsmvc.sec.service;

import com.douiou0.patientsmvc.sec.entities.AppRole;
import com.douiou0.patientsmvc.sec.entities.AppUser;
import com.douiou0.patientsmvc.sec.repositories.AppRoleRepository;
import com.douiou0.patientsmvc.sec.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j //?
@AllArgsConstructor
@Transactional
public class SecurityServiceImpl implements SecurityService {
    private AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

   /* Deja fait avec annotation en haut
   public SecurityServiceImpl(AppRoleRepository appRoleRepository,
                               AppUserRepository appUserRepository)
    {
        this.appRoleRepository = appRoleRepository;
        this.appUserRepository = appUserRepository;
    }*/

    @Override
    public AppUser saveNewUser(String username, String password, String rePassword) {
        if(!password.equals(rePassword)) throw new RuntimeException("Password not match");
        String hashedPWD = passwordEncoder.encode(password);
        AppUser appUser = new AppUser();
        appUser.setUserId(UUID.randomUUID().toString());
        appUser.setUsername(username);
        appUser.setPassword(hashedPWD);
        appUser.setActive(true);
        AppUser savedAppUser =appUserRepository.save(appUser);//savedAppUser pour l affichage
        return savedAppUser;
    }

    @Override
    public AppRole saveNewRole(String roleName, String description) {
        AppRole appRole =appRoleRepository.findByRoleName(roleName);
        if (appRole != null) throw new RuntimeException("Role"+roleName+" already exists");
        appRole = new AppRole();
        appRole.setRoleName(roleName);
        appRole.setDescription(description);
        AppRole savedAppRole= appRoleRepository.save(appRole);
        return savedAppRole;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) throw new RuntimeException("User not found");
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole == null) throw new RuntimeException("Role not found");
        appUser.getAppRoles().add(appRole);// ← Modification en mémoire
        // FIN DE MÉTHODE → Transaction se termine → AUTO-FLUSH → Changements SAUVEGARDÉS ✅
        //appUserRepository.save(appUser); //est ce que ne le besoin pas a faire ca avec transactionnel
    }

    @Override
    public void removeRoleFromUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) throw new RuntimeException("User not found");
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        if (appRole == null) throw new RuntimeException("Role not found");
        appUser.getAppRoles().remove(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {

        return appUserRepository.findByUsername(username);
    }

    @Override
    public boolean userExists(String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        return appUser != null;
    }
}



/** a reviser
 * @Transactional : Gestion des transactions Spring
 *
 * ██████████████████████████████████████████████
 *
 * ROLE PRINCIPAL :
 * - Garantir l'intégrité des opérations sur la base de données
 * - "Tout ou rien" : soit toutes les opérations réussissent, soit aucune n'est appliquée
 *
 * AVANTAGES :
 * ✓ Cohérence des données
 * ✓ Gestion automatique des sessions Hibernate/JPA
 * ✓ Rollback automatique en cas d'erreur
 * ✓ Contrôle de l'isolation et de la propagation
 *
 * MÉCANISMES IMPORTANTS :
 * 1. PROPAGATION (par défaut: REQUIRED)
 *    - Crée une nouvelle transaction ou rejoint une existante
 *
 * 2. ISOLATION (par défaut: niveau de la base)
 *    - Contrôle comment les transactions concurrentes voient les données
 *
 * 3. READONLY (par défaut: false)
 *    - Optimisation pour les requêtes de lecture seule
 *
 * 4. ROLLBACK (par défaut: RuntimeException)
 *    - Définit quelles exceptions déclenchent un rollback
 *
 * BONNES PRATIQUES :
 * - Placer @Transactional au niveau Service (pas Repository)
 * - Utiliser readOnly=true pour les méthodes de lecture
 * - Éviter les appels transactionnels longs (timeout)
 * - Tester le comportement de rollback
 *
 * EXEMPLE TYPE :
 * @Transactional
 * public void transferMoney() {
 *     retirer(compteA, 100);  // Si cette ligne réussit
 *     // et que cette ligne ↓ échoue
 *     deposer(compteB, 100);  // ALORS retirer() est annulé (rollback)
 * }
 *
 * ATTENTION :
 * - Les exceptions checked n'ont PAS de rollback automatique
 * - Une méthode private avec @Transactional ne fonctionne pas (proxy Spring)
 * - Performance : créer une transaction a un coût
 *
 * ALTERNATIVES :
 * @Transactional(readOnly = true)   ← Pour les requêtes SELECT
 * @Transactional(propagation = Propagation.REQUIRES_NEW) ← Nouvelle transaction indépendante
 * @Transactional(rollbackFor = Exception.class) ← Rollback pour toutes les exceptions
 *
 * ██████████████████████████████████████████████
 *
 * EN RÉSUMÉ :
 * @Transactional = Assurance "tout ou rien" pour vos opérations BD
 *                 La police d'assurance de vos données !
 */