package com.qtec.homestay.view.login.login;


import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.view.LoadDataView;

/**
 * @author shaojun
 * @name LoginView
 * @package com.fernandocejas.android10.sample.presentation.view
 * @date 15-9-10
 */
public interface LoginView extends LoadDataView {
  void openMain(LoginResponse response);

  void showUsernameEmp();

  void showPasswordEmp();

  void showPasswordErrorThreeTimes(Throwable e);

  void showPasswordErrorMoreTimes(Throwable e);
}
