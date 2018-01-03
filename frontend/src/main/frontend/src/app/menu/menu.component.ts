import {Component, OnInit, ViewContainerRef} from '@angular/core';
import { ToastsManager } from 'ng2-toastr/ng2-toastr';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  constructor (private toast: ToastsManager, private vcr: ViewContainerRef) {
    this.toast.setRootViewContainerRef(vcr);
  };

  ngOnInit() {
  }

  enter() {
    this.toast.success('This is not goodnvm ernmv er,nmv e,nrmv erhjbvehrjbvejlhrhevberhjvberhjlvberjhvbelrj!', 'Oops!');
  }
}
