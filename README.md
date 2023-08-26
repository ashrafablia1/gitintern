# gitintern - Internships/intern platform 

* technologies/languages and Frameworks:
• Java
• Spring Boot(Spring MVC,Spring Data JPA,Spring Security)
• Thymeleaf
• MYSQL Database.
• HTML and CSS.
• Lombok


* Design Pattern and Architectural Structure:
Model-View-Controller (MVC):
• Models
• Repositories
• Services 
• Controllers
• View
• DTOs (Data Transfer Objects)

*Demo:
GitIntern: [gitintern.tech](https://gitintern.tech/)
- Company user: gitintern.company1@gmail.com
- Intern user:  gitintern.intern1@gmail.com
- password: 2468 

note: demo is not responsive or not friendly view on smaller devices, such as phones


# FULL OVERVIEW
1. Anonymous User Main Page:
Functionality: Unregistered users view internship opportunities.
• Main page displays available internships for browsing.
• Application possible only after registering an account.

2. Registration and Confirmation:
Functionality: Users can register as interns or companies.
• Users provide necessary details during registration.
• A confirmation email is sent upon successful registration.
• Users must confirm their email using the provided link.
• Confirmation link contains a JWT token(24H vaild) for registration validation.

3. Account Activation:
Functionality: Accounts remain inactive until email confirmation.
• Accounts cannot be accessed until email confirmation is complete.
• Expired confirmation links lead to account deletion, requiring re-registration.
• Successful confirmation deletes the JWT token and activates the account for login.

4. Authentication and Security:
Functionality: JWT tokens are used for authentication.
• JWT token (24H valid) generated upon successful login.
• Tokens are sent via cookies for subsequent requests.
• Token validity checked on every request using filter to ensure user authentication.
• Expired tokens are deleted, and users are redirected to the login page.

5. Role-Based Authorization:
Functionality: Users have roles (Intern or Company) with varying privileges.
• Interns and companies have separate roles.
• Role determines accessible features and actions.

6. Logged-In Interns Main Page:
Functionality: Logged-in Interns access internship posts.
• Interns can apply to internships once.
• Applying form retrieve intern information and resume from database with ability 
to edit information and re-upload mew resume.
• Applying form updates user information across the platform if intern change it.

7. Internship Application Management:
Functionality: Interns view and manage their internship applications.
• Dedicated page displays submitted applications and statuses.
• Dedicated page view the application.
• Interns receive emails for application approval/rejection.
• After application approval companies will contact interns for next steps (interview,
exam).

8. Company Functionality:
Functionality: Companies can post, view, and manage internships.
• Companies can create new internship opportunity.
• Internship can be viewed and edited by the respective company.
• Dedicated page displays All owned internships to the company.
• Received applications for each internship are accessible for every internship 
separately.
• Dedicated page view the application.

9. Application Status Update:
Functionality: Companies can approve or reject applications.
• Companies can update application statuses from default (pending) to (approved, 
rejected).
• After the update, interns receive emails for application approval/rejection.

10. Company Profile:
Functionality: Companies have profile pages with editable information.
• Profile page contains company details.
• Companies can edit and view their information.

10. Intern Profile:
Functionality: Interns have profile pages with editable information.
• Profile page contains intern details and resume view or upload.
• Interns can edit and view their information.

-----------------------------------------------------------------------------------
Contact:
- Email: ashraf.ablia.jo@gmail.com
- Linkedin: [Ashraf Ablia](https://www.linkedin.com/in/ashraf-ablia/)

Enjoy coding!


