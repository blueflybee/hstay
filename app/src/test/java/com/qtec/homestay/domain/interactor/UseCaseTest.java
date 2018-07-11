/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qtec.homestay.domain.interactor;


import com.qtec.homestay.domain.executor.PostExecutionThread;
import com.qtec.homestay.domain.executor.ThreadExecutor;
import com.qtec.homestay.domain.params.IRequest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UseCaseTest {

  private UseCaseTestClass useCase;

  private TestDisposableObserver<Object> testObserver;

  @Mock private ThreadExecutor mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    this.useCase = new UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread);
    this.testObserver = new TestDisposableObserver<>();
    given(mockPostExecutionThread.getScheduler()).willReturn(new TestScheduler());
  }

  @Test
  public void testBuildUseCaseObservableReturnCorrectResult() {
    useCase.execute(Params.EMPTY, testObserver);

    assertThat(testObserver.valuesCount).isZero();
  }

  @Test
  public void testSubscriptionWhenExecutingUseCase() {
    useCase.execute(Params.EMPTY, testObserver);
    useCase.dispose();

    assertThat(testObserver.isDisposed()).isTrue();
  }

  @Test
  public void testShouldFailWhenExecuteWithNullObserver() {
    expectedException.expect(NullPointerException.class);
    useCase.execute(Params.EMPTY, null);
  }

  private static class UseCaseTestClass extends UseCase<Object> {

    UseCaseTestClass(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
      super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<Object> buildUseCaseObservable(IRequest param) {
      return Observable.empty();
    }

    @Override
    public void execute(IRequest params, DisposableObserver<Object> observer) {
      super.execute(params, observer);
    }
  }

  private static class TestDisposableObserver<T> extends DisposableObserver<T> {
    private int valuesCount = 0;

    @Override public void onNext(T value) {
      valuesCount++;
    }

    @Override public void onError(Throwable e) {
      // no-op by default.
    }

    @Override public void onComplete() {
      // no-op by default.
    }
  }

  private static class Params implements IRequest{
    private static Params EMPTY = new Params();
    private Params() {}
  }
}
