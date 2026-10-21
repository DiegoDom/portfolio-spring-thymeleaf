INSERT INTO personal_info (first_name, last_name, title, profile_description, profile_image_url, years_of_experience, email, phone, linkedin_url, github_url) VALUES
('Diego Alberto', 'Dominguez', 'Full Stack Developer', 'Apasionado por el desarrollo web con experiencia en Java, Spring Boot y React. Disfruto construyendo soluciones robustas y escalables.', 'img/profile-placeholder.jpg', 5, 'juan.perez@example.com', '+523348385911', 'https://www.linkedin.com/in/diegodom/', 'https://github.com/DiegoDom');

INSERT INTO skills (name, level_percentage, icon_class, personal_info_id) VALUES
('Java', 80, 'fab fa-java', 1),
('Spring Boot', 70, 'fas fa-leaf', 1),
('PostgreSQL', 80, 'fas fa-database', 1),
('HTML', 95, 'fab fa-html5', 1),
('CSS', 90, 'fab fa-css3-alt', 1),
('JavaScript', 75, 'fab fa-js-square', 1),
('React', 90, 'fab fa-react', 1);

INSERT INTO educations (degree, institution, start_date, end_date, description, personal_info_id) VALUES
('Ingeniería en Sistemas', 'Universidad XYZ', '2015-03-01', '2020-12-15', 'Especialización en desarrollo de software y bases de datos.', 1),
('Curso de Spring Boot Avanzado', 'Plataforma ABC', '2021-01-10', '2021-06-30', 'Profundización en microservicios y seguridad.', 1);

INSERT INTO experiences (job_title, company_name, start_date, end_date, description, personal_info_id) VALUES
('Desarrollador Full Stack Senior', 'Tech Solutions S.A.', '2022-01-01', NULL, 'Desarrollo y mantenimiento de aplicaciones empresariales. Liderazgo técnico de equipo de 3 personas.', 1),
('Desarrollador Backend Junior', 'Innovatech Labs', '2020-09-01', '2021-12-31', 'Participación en el desarrollo de APIs RESTful y optimización de bases de datos.', 1);