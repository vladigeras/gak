import {BrowserModule, Title} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from "./app.component";
import {MenuComponent} from './component/menu/menu.component';
import {FooterComponent} from './component/footer/footer.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {HttpClientModule} from "@angular/common/http";
import {ToastModule, ToastOptions} from 'ng2-toastr/ng2-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LoginModalComponent} from "./component/modal/login-modal/login-modal.component";
import {UserAddModalComponent} from "./component/modal/user-add-modal/user-add-modal.component";
import {UserService} from "./service/user.service";
import {AngularMultiSelectModule} from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import {FormsModule} from "@angular/forms";
import {UsersTableComponent} from './component/users-table/users-table.component';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {AuthService} from "./security/auth.service";
import {CookieService} from 'ngx-cookie-service';
import {AdminPanelComponent} from './pages/admin-panel/admin-panel.component';
import {MainPageComponent} from './pages/main-page/main-page.component';
import {RouterModule, Routes} from "@angular/router";
import {NotFoundPageComponent} from './pages/not-found-page/not-found-page.component';
import {MemberPanelComponent} from './pages/member-panel/member-panel.component';
import {SecretaryPanelComponent} from './pages/secretary-panel/secretary-panel.component';
import {PresidentPanelComponent} from './pages/president-panel/president-panel.component';
import {DndModule} from 'ng2-dnd';
import {StudentSpeechByDateComponent} from './component/student-speech-by-date/student-speech-by-date.component';
import {StudentService} from "./service/student.service";
import {StudentAddModalComponent} from './component/modal/student-add-modal/student-add-modal.component';
import {StudentsTableComponent} from './component/students-table/students-table.component';
import {OWL_DATE_TIME_LOCALE, OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
import {SpeakerService} from "./service/speaker.service";
import {CommissionsTableComponent} from './component/commissions-table/commissions-table.component';
import {CommissionService} from "./service/commission.service";
import {SpeakersStudentTableComponent} from './component/speakers-student-table/speakers-student-table.component';
import {QuestionService} from "./service/question.service";
import {GuardService} from "./security/guard.service";
import {CriteriaComponent} from "./component/criteria/criteria.component";
import {CriteriaService} from "./service/criteria.service";
import {BlockUIModule} from 'ng-block-ui';
import {SocketService} from "./service/socket.service";
import {DiplomService} from "./service/diplom.service";



import {StopWatchService} from "./service/stopwatch.service";
import {StopWatchComponent} from "./component/stopwatch/stopwatch.component";
import {TransfereService} from "./service/transfere.service";

export class CustomToastOptions extends ToastOptions {
  showCloseButton = true;
  enableHTML = true;
  maxShown = 20;
}

export let waitString = "Подождите";
export let socketPath = "http://" + window.location.hostname + ":8080/socket";   //for example http://localhost:8080/socket

const routes: Routes = [
  {path: '', component: MainPageComponent},
  {path: 'president', component: PresidentPanelComponent, data: {roles: ["PRESIDENT"]}, canActivate: [GuardService]},
  {path: 'member', component: MemberPanelComponent, data: {roles: ["MEMBER"]}, canActivate: [GuardService]},
  {path: 'secretary', component: SecretaryPanelComponent, data: {roles: ["SECRETARY"]}, canActivate: [GuardService]},
  {path: 'admin', component: AdminPanelComponent, data: {roles: ["ADMIN"]}, canActivate: [GuardService]},
  {path: '**', component: NotFoundPageComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    FooterComponent,
    LoginModalComponent,
    UserAddModalComponent,
    UsersTableComponent,
    AdminPanelComponent,
    MainPageComponent,
    NotFoundPageComponent,
    MemberPanelComponent,
    SecretaryPanelComponent,
    PresidentPanelComponent,
    StudentSpeechByDateComponent,
    StudentAddModalComponent,
    StudentsTableComponent,
    CommissionsTableComponent,
    SpeakersStudentTableComponent,
    CriteriaComponent,
    StopWatchComponent

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    NgbModule.forRoot(),
    ToastModule.forRoot(),
    AngularMultiSelectModule,
    NgxDatatableModule,
    RouterModule.forRoot(routes, {useHash: true}),
    DndModule.forRoot(),
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    BlockUIModule
  ],
  providers: [
    {provide: ToastOptions, useClass: CustomToastOptions},
    UserService,
    AuthService,
    CookieService,
    StudentService,
    SpeakerService,
    CommissionService,
    QuestionService,
    {provide: OWL_DATE_TIME_LOCALE, useValue: 'ru'},
    GuardService,
    CriteriaService,
    SocketService,
    DiplomService,
    StopWatchService,
    TransfereService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
