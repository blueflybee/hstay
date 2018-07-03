package com.qtec.homestay.domain.interactor.cloud;

import com.qtec.homestay.domain.executor.PostExecutionThread;
import com.qtec.homestay.domain.executor.ThreadExecutor;
import com.qtec.homestay.domain.interactor.UseCase;
import com.qtec.homestay.domain.model.mapp.rsp.LoginResponse;
import com.qtec.homestay.domain.params.IRequest;
import com.qtec.homestay.domain.repository.CloudRepository;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * 登录用例
 *
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class LoginUseCase extends UseCase<LoginResponse> {

  private final CloudRepository cloudRepository;

  @Inject public LoginUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override
  protected Observable<LoginResponse> buildUseCaseObservable(IRequest param) {
    return cloudRepository.login(param);
  }
}
