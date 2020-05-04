import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-backend',
  templateUrl: './backend.component.html',
  styleUrls: ['./backend.component.css']
})
export class BackendComponent implements OnInit {

  data: any;

  constructor() { }

  ngOnInit() {
  }

  getData() {
    this.data = "Rest call needs to be implemented."
  }

}
