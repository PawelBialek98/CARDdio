package pl.lodz.pl.it.cardio.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import pl.lodz.pl.it.cardio.exception.AppTransactionFailureException;
import pl.lodz.pl.it.cardio.service.UserService;

import javax.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AccountScheduler {

    private final UserService userService;

    @Scheduled(cron = "${cron.deleteInactiveUsers}", zone = "Europe/Warsaw")
    public void removeInactivatedAccounts() {

        Logger.getGlobal().log(Level.INFO, "Remove inactivated accounts script - strted");
        try{
            userService.removeInactivatedAccounts();
        } catch (AppTransactionFailureException e){
            Logger.getGlobal().log(Level.INFO, "Remove inactivated accounts script - failed!\nMessage:\n" + e.getMessage());
        }
        Logger.getGlobal().log(Level.INFO, "Remove inactivated accounts script - finished");
    }

}
