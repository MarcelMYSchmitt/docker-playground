import { Component, OnInit, AfterViewInit } from '@angular/core';
import { MatomoTracker } from 'ngx-matomo'

@Component({
  selector: 'app-landingpage',
  templateUrl: './landingpage.component.html',
  styleUrls: ['./landingpage.component.css']
})
export class LandingpageComponent implements OnInit, AfterViewInit {

  constructor(private matomoTracker: MatomoTracker) {
  }
  /**
 * OnInit lifecycle hook
 */
  ngOnInit() {
    // user id should not be hardcoded
      this.matomoTracker.setUserId('1234');

    //this.matomoTracker.setDocumentTitle('Landing Page');
  }

  /**
   * AfterViewInit lifecycle hook
   */
  ngAfterViewInit() {

    try {
      this.matomoTracker.trackPageView('Landing Page');
      // track event could have a counter 
      this.matomoTracker.trackEvent('Visit', 'View', 'Landing Page', 1);
  
      this.matomoTracker.getUserId().then((userId: string) => console.log('User ID:', userId));
      this.matomoTracker
        .getVisitorId()
        .then((visitorId: string) => console.log('Visitor ID:', visitorId));
      this.matomoTracker
        .getVisitorInfo()
        .then((visitorInfo: string[]) => console.log('Visitor Info:', visitorInfo));
      this.matomoTracker
        .hasCookies()
        .then((hasCookies: boolean) => console.log('Has Cookies:', hasCookies));
    } catch{
console.log("asdsad")
    }
   
  }
}
