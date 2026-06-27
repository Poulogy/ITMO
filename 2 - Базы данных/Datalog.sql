BEGIN;
CREATE TYPE anim_type AS ENUM ('Tyranozavr','Dinozavr');
CREATE TYPE sizee AS ENUM ('большой','маленький');
CREATE TYPE quantity AS ENUM ('много','мало');

CREATE TABLE IF NOT EXISTS Person(person_id SERIAL PRIMARY KEY, location TEXT NOT NULL, name TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS Shadow(shadow_id SERIAL PRIMARY KEY,
size_of_shadow sizee NOT NULL);

CREATE TABLE IF NOT EXISTS Animal(animal_id SERIAL PRIMARY KEY,
type_of_animal anim_type NOT NULL,
anim_size sizee NOT NULL);

CREATE TABLE IF NOT EXISTS Interaction(interaction_id SERIAL PRIMARY KEY,
id_whodid INT REFERENCES Animal,
id_towhomdid INT REFERENCES Person,
what_interaction TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS GroupOfAutos(PRIMARY KEY (shadow_id),
shadow_id INT REFERENCES Shadow,
auto_location TEXT NOT NULL,
quantity quantity NOT NULL);

CREATE TABLE IF NOT EXISTS Implement(PRIMARY KEY (shadow_id, animal_id),
shadow_id INT REFERENCES Shadow,
animal_id INT REFERENCES Animal,
Who_to_whom TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS Conclusion(concl_id SERIAL PRIMARY KEY,
shadow_id INT REFERENCES Shadow,
person_id INT REFERENCES Person,
Conclusion_text TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS Memory(PRIMARY KEY (person_id, interaction_id),
person_id INT REFERENCES Person,
interaction_id INT REFERENCES Interaction,
actuality BOOL NOT NULL);

INSERT INTO Person(name, location)
VALUES ('Грант', 'Комната');

INSERT INTO Animal(type_of_animal, anim_size)
VALUES ('Tyranozavr', 'большой');

INSERT INTO Animal(type_of_animal, anim_size)
VALUES ('Tyranozavr', 'маленький');

INSERT INTO Shadow(size_of_shadow)
VALUES ('маленький');

INSERT INTO GroupOfAutos(shadow_id, quantity, auto_location)
VALUES (1, 'много', 'Гараж');

INSERT INTO Conclusion(shadow_id, person_id, Conclusion_text)
VALUES (1, 1, 'Тень это маленький Тиранозавр');

INSERT INTO Interaction(id_whodid, id_towhomdid, what_interaction)
VALUES (1, 1,'Напал');

INSERT INTO Memory(person_id, interaction_id, actuality)
VALUES (1, 1,'True');

INSERT INTO Implement(animal_id, shadow_id, Who_to_Whom)
VALUES (2, 1,'Тень принадлежит муленькому Тиранозвру');

END;



MNGNZHS14065