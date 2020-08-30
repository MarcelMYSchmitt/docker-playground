import { BrowserModule } from '@angular/platform-browser';
import { NgModule, Inject } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MatomoComponent } from './matomo/matomo.component';

import { MatomoModule } from 'ngx-matomo';
import { LandingpageComponent } from './landingpage/landingpage.component';
import { ElkComponent } from './elk/elk.component';

import { ApmService } from '@elastic/apm-rum-angular'
import { ErrorHandler } from '@angular/core'
import { ApmErrorHandler } from '@elastic/apm-rum-angular'
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';

import { throwError, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { WebsocketsComponent } from './websockets/websockets.component';
import { BackendComponent } from './backend/backend.component';
import { SentryErrorHandler } from './sentry/sentry.error.service';
import { SentryComponent } from './sentry/sentry.component';


@NgModule({
  declarations: [
    AppComponent,
    MatomoComponent,
    LandingpageComponent,
    ElkComponent,
    WebsocketsComponent,
    BackendComponent,
    SentryComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    MatomoModule
  ],
  providers: [
    {
      provide: ApmService,
      useClass: ApmService,
      deps: [Router]
    },
    {
      provide: ErrorHandler,
      //useClass: ApmErrorHandler
      useClass: SentryErrorHandler

    }],
  bootstrap: [AppComponent]
})

export class AppModule {

  private elasticUrl = 'http://localhost:8200';

  constructor(@Inject(ApmService) apmService, private httpClient: HttpClient) {

    this.httpClient.get(this.elasticUrl)
      .subscribe(
        data => this.InitializeConnectionToElastic(apmService),
        err => console.log("Elastic Search via " + this.elasticUrl + " not available. Is it running?"),
        () => console.log('yay....')
      );
  }

  InitializeConnectionToElastic(apmService: ApmService) {
    console.log("Establish Connection to Elastic Search for Logging APM...");

    const apm = apmService.init({
      serviceName: 'angular-lama-app',
      serverUrl: this.elasticUrl
    });

    apm.setUserContext({
      'username': 'lama-name',
      'id': 'lama-id'
    });
  }
}
