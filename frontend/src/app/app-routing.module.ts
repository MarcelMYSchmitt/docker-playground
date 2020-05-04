import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MatomoComponent } from './matomo/matomo.component';
import { LandingpageComponent } from './landingpage/landingpage.component';
import { ElkComponent } from './elk/elk.component';
import { WebsocketsComponent } from './websockets/websockets.component';

const routes: Routes = [
  { path: '', component: LandingpageComponent },
  { path: 'matomo', component: MatomoComponent },
  { path: 'elk', component: ElkComponent },
  { path: 'websockets', component: WebsocketsComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
