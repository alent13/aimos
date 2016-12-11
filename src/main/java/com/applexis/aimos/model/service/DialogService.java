package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Dialog;

/**
 * @author applexis
 */
public interface DialogService {

    Dialog createDialog(Dialog dialog);

    Dialog getDialog(Long id);

    void delete(Dialog user);
    void delete(Long id);

    Dialog update(Dialog dialog);

}
