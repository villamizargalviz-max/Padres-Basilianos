package com.example.data.repository

import com.example.data.model.BasilianCouncilMember
import com.example.data.model.BasilianDirectoryMember
import com.example.data.model.BasilianHouseInfo
import com.example.data.model.DeceasedBrother
import com.example.data.model.DeceasedCategory
import com.example.data.model.MemberCategory

object BasilianDirectoryRepository {

    val members: List<BasilianDirectoryMember> = listOf(
        // =====================================================================
        // A
        // =====================================================================
        BasilianDirectoryMember(
            id = "abercrombie_jamie",
            name = "Abercrombie, Jamie Mitchell",
            birthDateRaw = "06 September 1938",
            birthDay = 6, birthMonth = 9, birthYear = 1938,
            professionDate = "08 September 1967",
            ordinationDate = "09 June 1973",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Silverado Hermann Park Memory Care",
            address = "5600 Chenevert St., Houston, TX 77004",
            cellPhone = "281-900-7824"
        ),
        BasilianDirectoryMember(
            id = "april_denis",
            name = "April, Denis Joseph",
            birthDateRaw = "08 May 1941",
            birthDay = 8, birthMonth = 5, birthYear = 1941,
            professionDate = "15 August 1960",
            ordinationDate = "10 July 1971",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "148 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 148",
            email = "denisapril@gmail.com"
        ),

        // =====================================================================
        // B
        // =====================================================================
        BasilianDirectoryMember(
            id = "baenziger_edward",
            name = "Baenziger, Edward James",
            birthDateRaw = "29 July 1946",
            birthDay = 29, birthMonth = 7, birthYear = 1946,
            professionDate = "15 August 1965",
            ordinationDate = "01 May 1976",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "713-444-9116",
            email = "tbaenziger@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "balderas_alejandro",
            name = "Balderas Rosas, Alejandro",
            birthDateRaw = "14 August 1991",
            birthDay = 14, birthMonth = 8, birthYear = 1991,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 23 8274 5158",
            email = "alex.bal.rosas@outlook.es"
        ),
        BasilianDirectoryMember(
            id = "barringer_robert",
            name = "Barringer, Robert Joseph",
            birthDateRaw = "17 May 1944",
            birthDay = 17, birthMonth = 5, birthYear = 1944,
            professionDate = "15 September 1963",
            ordinationDate = "18 May 1974",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "471 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 471",
            email = "barringerr9@gmail.com",
            rolesOrMinistries = "Ex Superior General (1993-1997)"
        ),
        BasilianDirectoryMember(
            id = "basil_david",
            name = "Basil, David Francis Semivule",
            birthDateRaw = "23 September 1990",
            birthDay = 23, birthMonth = 9, birthYear = 1990,
            professionDate = "08 August 2020",
            category = MemberCategory.DEACON,
            country = "Canadá",
            currentHouse = "Our Lady of the Assumption Parish",
            address = "350 Huron Church Rd., Windsor, ON N9C 2J9",
            cellPhone = "382-342-0706",
            email = "dbasil@basilian.org",
            rolesOrMinistries = "Diácono • Próxima Ordenación Sacerdotal en Windsor"
        ),
        BasilianDirectoryMember(
            id = "belisch_carl",
            name = "Belisch, Carl Louis",
            birthDateRaw = "24 December 1935",
            birthDay = 24, birthMonth = 12, birthYear = 1935,
            professionDate = "15 August 1955",
            ordinationDate = "11 December 1965",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "346-204-8786",
            email = "cbelisch1@basilian.org"
        ),
        BasilianDirectoryMember(
            id = "bittner_david",
            name = "Bittner, David Craig",
            birthDateRaw = "06 July 1955",
            birthDay = 6, birthMonth = 7, birthYear = 1955,
            professionDate = "12 August 1979",
            ordinationDate = "12 May 1984",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Anne's Parish",
            address = "2140 Westheimer Rd., Houston, TX 77098",
            cellPhone = "713-208-5780",
            email = "dbittnercsb@gmail.com",
            rolesOrMinistries = "Comité de Formación"
        ),
        BasilianDirectoryMember(
            id = "blocher_james",
            name = "Blocher, James Francis",
            birthDateRaw = "04 May 1946",
            birthDay = 4, birthMonth = 5, birthYear = 1946,
            professionDate = "15 August 1965",
            ordinationDate = "14 June 1975",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Mother of Good Counsel Home",
            address = "6825 Natural Bridge Rd., St. Louis, MO 63121",
            cellPhone = "281-705-8000",
            email = "jblocher@basilian.org"
        ),
        BasilianDirectoryMember(
            id = "brand_michael",
            name = "Brand, Michael Paul",
            birthDateRaw = "16 April 1943",
            birthDay = 16, birthMonth = 4, birthYear = 1943,
            professionDate = "15 August 1962",
            ordinationDate = "19 May 1973",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Amica Riverside",
            address = "1106 - 4909 Riverside Dr. E., Windsor, ON N8Y 0A4",
            cellPhone = "519-915-0913",
            email = "mpbrnd@aol.com"
        ),
        BasilianDirectoryMember(
            id = "buentello_michael",
            name = "Buentello, Michael Anthony",
            birthDateRaw = "03 March 1958",
            birthDay = 3, birthMonth = 3, birthYear = 1958,
            professionDate = "10 August 1986",
            ordinationDate = "13 May 1995",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Las Cruces",
            address = "1682 Alta Vista Place, Las Cruces, NM 88011",
            cellPhone = "219-614-3802",
            email = "mikeabuentello@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "buisson_bernard",
            name = "Buisson, Bernard Marcel",
            birthDateRaw = "19 April 1949",
            birthDay = 19, birthMonth = 4, birthYear = 1949,
            professionDate = "12 September 1968",
            ordinationDate = "15 December 1979",
            category = MemberCategory.PRIEST,
            country = "Francia",
            currentHouse = "EHPAD Saint-Joseph à Varagnes",
            address = "51 Chemin de la Convalescence, 07100 Annonay, France",
            officePhone = "(011 33) 04 75 32 42 32",
            rolesOrMinistries = "Comunidad de Annonay, cuna histórica de la Congregación"
        ),
        BasilianDirectoryMember(
            id = "burns_leo",
            name = "Burns, Leo Dennis",
            birthDateRaw = "21 March 1934",
            birthDay = 21, birthMonth = 3, birthYear = 1934,
            professionDate = "15 August 1953",
            ordinationDate = "13 December 1964",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "156 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 156",
            rolesOrMinistries = "Hermano Nonagenario (91 años)"
        ),

        // =====================================================================
        // C
        // =====================================================================
        BasilianDirectoryMember(
            id = "callam_daniel",
            name = "Callam, Daniel Doherty",
            birthDateRaw = "12 June 1935",
            birthDay = 12, birthMonth = 6, birthYear = 1935,
            professionDate = "15 August 1955",
            ordinationDate = "13 December 1964",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "467 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 467",
            email = "ddcallam@gmail.com",
            rolesOrMinistries = "Hermano Nonagenario (90 años)"
        ),
        BasilianDirectoryMember(
            id = "chircop_manuel",
            name = "Chircop, Manuel Joseph",
            birthDateRaw = "25 December 1943",
            birthDay = 25, birthMonth = 12, birthYear = 1943,
            professionDate = "29 September 1972",
            ordinationDate = "11 June 1977",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Garden City",
            address = "1204 Deering St., Garden City, MI 48135",
            cellPhone = "313-530-3222",
            email = "mjchircopjd@gmail.com"
        ),

        // =====================================================================
        // D
        // =====================================================================
        BasilianDirectoryMember(
            id = "dawood_bishoy",
            name = "Dawood, Bishoy",
            birthDateRaw = "05 February 1982",
            birthDay = 5, birthMonth = 2, birthYear = 1982,
            professionDate = "08 July 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 55 2244 6252",
            email = "bdawood@basilian.org",
            rolesOrMinistries = "Escolástico en Formación Teológica"
        ),
        BasilianDirectoryMember(
            id = "de_la_hoz_osman",
            name = "De La Hoz Escorcia, Osman Enrique",
            birthDateRaw = "09 April 1987",
            birthDay = 9, birthMonth = 4, birthYear = 1987,
            professionDate = "01 January 2020",
            ordinationDate = "02 January 2025",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Basilian Fathers of Houma",
            address = "100 Lumen Christi Court, Schriever, LA 70395",
            cellPhone = "985-217-6311",
            email = "osende_2008_@hotmail.com",
            rolesOrMinistries = "Misión Intercultural Houma-Thibodaux"
        ),
        BasilianDirectoryMember(
            id = "dowalgo_mitchell",
            name = "Dowalgo, Mitchell George",
            birthDateRaw = "15 May 1952",
            birthDay = 15, birthMonth = 5, birthYear = 1952,
            professionDate = "15 August 1976",
            ordinationDate = "13 February 1982",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "575-202-9846",
            email = "mdesertbud@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "dugan_thomas",
            name = "Dugan, Thomas Patrick",
            birthDateRaw = "26 June 1929",
            birthDay = 26, birthMonth = 6, birthYear = 1929,
            professionDate = "12 September 1951",
            ordinationDate = "29 June 1960",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Sisters of Saint Joseph",
            address = "150 French Rd. #2232, Rochester, NY 14618",
            cellPhone = "585-773-0004",
            email = "tpdugan7@gmail.com",
            rolesOrMinistries = "Hermano Nonagenario (96 años)"
        ),
        BasilianDirectoryMember(
            id = "dulock_vincent",
            name = "Dulock, Vincent Joseph",
            birthDateRaw = "18 February 1940",
            birthDay = 18, birthMonth = 2, birthYear = 1940,
            professionDate = "15 August 1959",
            ordinationDate = "20 December 1969",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "713-306-8794",
            email = "vdulock@earthlink.net"
        ),

        // =====================================================================
        // E
        // =====================================================================
        BasilianDirectoryMember(
            id = "english_paul",
            name = "English, Paul Francis",
            birthDateRaw = "22 January 1956",
            birthDay = 22, birthMonth = 1, birthYear = 1956,
            professionDate = "16 August 1981",
            ordinationDate = "11 May 1985",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Anne's Parish",
            address = "2140 Westheimer Rd., Houston, TX 77098",
            cellPhone = "585-754-4777",
            email = "penglish@basilian.org",
            rolesOrMinistries = "Cuarto Consejero General • Director de Formación Inicial"
        ),
        BasilianDirectoryMember(
            id = "enriquez_erick",
            name = "Enríquez Gutiérrez, Erick de Jesús",
            birthDateRaw = "23 June 1982",
            birthDay = 23, birthMonth = 6, birthYear = 1982,
            professionDate = "18 July 2020",
            ordinationDate = "14 September 2024",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Escolasticado Basiliano / San Pedro Mártir",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 73 5212 4904",
            email = "erickcsb1822@gmail.com",
            rolesOrMinistries = "Vicario San Pedro de Verona, Mártir"
        ),
        BasilianDirectoryMember(
            id = "espejel_rene",
            name = "Espejel González, René Javier",
            birthDateRaw = "11 March 1955",
            birthDay = 11, birthMonth = 3, birthYear = 1955,
            professionDate = "16 July 1979",
            ordinationDate = "06 June 1987",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Casa San Felipe",
            address = "Lázaro Cárdenas #715, San Lorenzo Teotipilco, 75855 Tehuacán, Puebla",
            cellPhone = "+52 83 2506 0483",
            email = "espejelrene@yahoo.com",
            rolesOrMinistries = "Rector Casa San Felipe 2025"
        ),
        BasilianDirectoryMember(
            id = "estrada_alejandro",
            name = "Estrada Pérez, Alejandro",
            birthDateRaw = "04 September 1985",
            birthDay = 4, birthMonth = 9, birthYear = 1985,
            professionDate = "15 August 2012",
            ordinationDate = "05 January 2019",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Parroquia de San Lorenzo",
            address = "Calle Benito Juárez Sur #316, 75855 Tehuacán, Puebla",
            cellPhone = "+52 55 4448 1671",
            email = "alex.estrada.perez@gmail.com",
            rolesOrMinistries = "Vicario Parroquia San Lorenzo Teotipilco"
        ),

        // =====================================================================
        // F
        // =====================================================================
        BasilianDirectoryMember(
            id = "fabbro_ronald",
            name = "Fabbro, + Ronald Peter",
            birthDateRaw = "06 November 1950",
            birthDay = 6, birthMonth = 11, birthYear = 1950,
            professionDate = "15 August 1976",
            ordinationDate = "03 May 1980",
            category = MemberCategory.BISHOP,
            country = "Canadá",
            currentHouse = "Diocese of London",
            address = "1070 Waterloo St., London, ON N6A 3Y2",
            officePhone = "519-433-0658",
            email = "bishop@dol.ca",
            rolesOrMinistries = "Obispo de la Diócesis de London • Ex Superior General (1997-2002)"
        ),
        BasilianDirectoryMember(
            id = "fallona_michael",
            name = "Fallona, Michael John Anthony",
            birthDateRaw = "13 June 1938",
            birthDay = 13, birthMonth = 6, birthYear = 1938,
            professionDate = "01 September 1963",
            ordinationDate = "03 July 1967",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "146 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "647-864-8471",
            email = "fallona@basilian.org"
        ),
        BasilianDirectoryMember(
            id = "farge_james",
            name = "Farge, James Knox",
            birthDateRaw = "22 August 1938",
            birthDay = 22, birthMonth = 8, birthYear = 1938,
            professionDate = "15 August 1960",
            ordinationDate = "21 May 1977",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "University of St. Michael’s College",
            address = "81 St. Mary St., Toronto, ON M5S 1J4",
            cellPhone = "647-561-6074",
            email = "james.farge@utoronto.ca",
            rolesOrMinistries = "Investigador Historiador USMC"
        ),
        BasilianDirectoryMember(
            id = "fulton_patrick",
            name = "Fulton, Patrick William",
            birthDateRaw = "21 November 1968",
            birthDay = 21, birthMonth = 11, birthYear = 1968,
            professionDate = "08 August 1993",
            ordinationDate = "20 September 1997",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Basilian Fathers of Novi",
            address = "49501 Harrier Place, Novi, MI 48374",
            cellPhone = "281-785-3337",
            email = "pwfulton@hotmail.com",
            rolesOrMinistries = "Rector de Novi • Principal Detroit Catholic Central High School"
        ),

        // =====================================================================
        // G
        // =====================================================================
        BasilianDirectoryMember(
            id = "gallagher_john",
            name = "Gallagher, John Clarence",
            birthDateRaw = "02 January 1934",
            birthDay = 2, birthMonth = 1, birthYear = 1934,
            professionDate = "15 August 1952",
            ordinationDate = "29 June 1960",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "415 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            email = "jgallagher@basilian.org",
            rolesOrMinistries = "Superior General Emérito (1989-1993) • Nonagenario (91 años)"
        ),
        BasilianDirectoryMember(
            id = "galvis_juan_gabriel",
            name = "Galvis Villamizar, Juan Gabriel",
            birthDateRaw = "01 October 2000",
            birthDay = 1, birthMonth = 10, birthYear = 2000,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá, Colombia",
            cellPhone = "+57 322 945 5735",
            email = "villamizargalviz@gmail.com, escolastico.bogota@basilianos.org",
            rolesOrMinistries = "Escolástico Basiliano en Bogotá • Misión Egipto • Líder Creador App C.S.B."
        ),
        BasilianDirectoryMember(
            id = "gazin_mark",
            name = "Gazin, Mark Ross",
            birthDateRaw = "22 July 1958",
            birthDay = 22, birthMonth = 7, birthYear = 1958,
            professionDate = "10 August 1985",
            ordinationDate = "29 June 1990",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "832-758-3150",
            email = "mgazin@basilian.org",
            rolesOrMinistries = "Vicario Judicial Adjunto, Arquidiócesis de Galveston-Houston"
        ),
        BasilianDirectoryMember(
            id = "giampietro_anthony",
            name = "Giampietro, Anthony Emile",
            birthDateRaw = "25 February 1961",
            birthDay = 25, birthMonth = 2, birthYear = 1961,
            professionDate = "13 August 1989",
            ordinationDate = "18 September 1993",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Patrick’s Seminary",
            address = "320 Middlefield Rd., Menlo Park, CA 94025",
            cellPhone = "203-589-8082",
            email = "aegcsb@gmail.com",
            rolesOrMinistries = "Ministerio Académico San Francisco"
        ),
        BasilianDirectoryMember(
            id = "glass_robert",
            name = "Glass, Robert Harrison",
            birthDateRaw = "08 April 1945",
            birthDay = 8, birthMonth = 4, birthYear = 1945,
            professionDate = "15 August 1963",
            ordinationDate = "18 May 1974",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "281-636-0558",
            email = "bob.glass@sths.org"
        ),
        BasilianDirectoryMember(
            id = "gomez_oscar",
            name = "Gómez Soto, Óscar Fernando",
            birthDateRaw = "24 January 1975",
            birthDay = 24, birthMonth = 1, birthYear = 1975,
            professionDate = "02 January 2003",
            ordinationDate = "02 January 2010",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Parroquia de San Lorenzo",
            address = "Calle Benito Juárez Sur #316, 75855 Tehuacán, Puebla",
            cellPhone = "+52 32 0676 4857",
            email = "ofgomez@gmail.com",
            rolesOrMinistries = "Vicario San Lorenzo Teotipilco"
        ),
        BasilianDirectoryMember(
            id = "gomez_jose_adan",
            name = "Gómez Villalvazo, José Adán",
            birthDateRaw = "29 April 1983",
            birthDay = 29, birthMonth = 4, birthYear = 1983,
            professionDate = "18 July 2020",
            ordinationDate = "14 September 2024",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Casa San Felipe",
            address = "Lázaro Cárdenas #715, 75855 Tehuacán, Puebla",
            cellPhone = "+52 31 2110 3595",
            email = "giossepe95@outlook.com"
        ),
        BasilianDirectoryMember(
            id = "griffin_ronald",
            name = "Griffin, Ronald John",
            birthDateRaw = "22 July 1940",
            birthDay = 22, birthMonth = 7, birthYear = 1940,
            professionDate = "15 August 1960",
            ordinationDate = "09 May 1971",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "St. Thomas More College",
            address = "1701 - 241 5th Ave. N., Saskatoon, SK S7K 2P3",
            email = "rgriffin@stmcollege.ca"
        ),
        BasilianDirectoryMember(
            id = "guerrero_eduard",
            name = "Guerrero Carrillo, Eduard Alexis",
            birthDateRaw = "13 November 1987",
            birthDay = 13, birthMonth = 11, birthYear = 1987,
            professionDate = "13 June 2021",
            ordinationDate = "02 January 2025",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Parroquia San Basilio",
            address = "Calle 58 #102-05, Barrio Olaya Herrera, Medellín",
            cellPhone = "+57 321 463 2767",
            email = "eduardguerrero25@gmail.com",
            rolesOrMinistries = "Promotor Vocacional Colombia"
        ),

        // =====================================================================
        // H
        // =====================================================================
        BasilianDirectoryMember(
            id = "hanna_jack",
            name = "Hanna, Jack Haldeman",
            birthDateRaw = "21 March 1944",
            birthDay = 21, birthMonth = 3, birthYear = 1944,
            professionDate = "15 September 1963",
            ordinationDate = "17 May 1974",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Frassati House",
            address = "124 Sackville St., Toronto, ON M5A 3E7",
            cellPhone = "647-551-9509",
            email = "hannaj@basilian.org",
            rolesOrMinistries = "Traductor Oficial de la Congregación"
        ),
        BasilianDirectoryMember(
            id = "heidt_edward",
            name = "Heidt, Edward Raymond",
            birthDateRaw = "20 October 1946",
            birthDay = 20, birthMonth = 10, birthYear = 1946,
            professionDate = "12 September 1966",
            ordinationDate = "29 May 1976",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Las Cruces",
            address = "1682 Alta Vista Place, Las Cruces, NM 88011",
            cellPhone = "575-520-0578",
            email = "ed.heidt@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "hibberd_neil",
            name = "Hibberd, Neil Byron",
            birthDateRaw = "28 July 1940",
            birthDay = 28, birthMonth = 7, birthYear = 1940,
            professionDate = "15 August 1959",
            ordinationDate = "13 December 1969",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "154 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            email = "n.hibberd@hotmail.ca"
        ),
        BasilianDirectoryMember(
            id = "higuera_javier",
            name = "Higuera Orjuela, Javier",
            birthDateRaw = "22 June 1985",
            birthDay = 22, birthMonth = 6, birthYear = 1985,
            professionDate = "02 January 2013",
            ordinationDate = "02 January 2020",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Parroquia San Basilio",
            address = "Calle 58 #102-05, Barrio Olaya Herrera, Medellín",
            cellPhone = "+57 312 306 4214",
            email = "javierhigueraorjuela@hotmail.com",
            rolesOrMinistries = "Párroco Parroquia San Basilio Medellín (2024)"
        ),
        BasilianDirectoryMember(
            id = "holmes_robert",
            name = "Holmes, Robert Kernahan",
            birthDateRaw = "06 July 1936",
            birthDay = 6, birthMonth = 7, birthYear = 1936,
            professionDate = "15 August 1955",
            ordinationDate = "13 December 1964",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "417 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "416-566-6006",
            email = "rholmes@basilian.org",
            rolesOrMinistries = "Comité JPIC"
        ),
        BasilianDirectoryMember(
            id = "hoyos_jose_enrique",
            name = "Hoyos Avilez, José Enrique",
            birthDateRaw = "22 February 1998",
            birthDay = 22, birthMonth = 2, birthYear = 1998,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 312 529 8976",
            email = "hoyosjosee@gmail.com",
            rolesOrMinistries = "Escolástico Basiliano en Bogotá"
        ),
        BasilianDirectoryMember(
            id = "huber_john",
            name = "Huber, John Brian",
            birthDateRaw = "01 December 1963",
            birthDay = 1, birthMonth = 12, birthYear = 1963,
            professionDate = "15 August 1987",
            ordinationDate = "15 August 1992",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Curia Generalicia - Toronto",
            address = "146 Davenport Road, Toronto, ON M5R 1J2",
            cellPhone = "713-518-6051",
            officePhone = "416-921-6674 ext. 202",
            email = "jhuber@basilian.org",
            rolesOrMinistries = "Superior General de la Congregación de San Basilio (Capítulo General 2026 - presente)"
        ),
        BasilianDirectoryMember(
            id = "huber_steven",
            name = "Huber, Steven Gerard",
            birthDateRaw = "29 June 1986",
            birthDay = 29, birthMonth = 6, birthYear = 1986,
            professionDate = "15 August 2013",
            ordinationDate = "09 February 2019",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Anne’s Parish",
            address = "2140 Westheimer Rd., Houston, TX 77098",
            cellPhone = "281-468-3187",
            email = "shuber865@gmail.com",
            rolesOrMinistries = "Vicario Parroquial St. Anne's • Promotor Vocaciones / Redes"
        ),
        BasilianDirectoryMember(
            id = "hyatt_donald",
            name = "Hyatt, Donald Frank",
            birthDateRaw = "22 March 1939",
            birthDay = 22, birthMonth = 3, birthYear = 1939,
            professionDate = "15 August 1958",
            ordinationDate = "14 December 1968",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Heritage House Assisted Living",
            address = "854 Lettuce Lane, Las Cruces, NM 88001",
            cellPhone = "505-440-6042",
            email = "aqspz@yahoo.com"
        ),

        // =====================================================================
        // I - J
        // =====================================================================
        BasilianDirectoryMember(
            id = "ibanez_antonio",
            name = "Ibáñez Santos, Antonio",
            birthDateRaw = "01 April 1978",
            birthDay = 1, birthMonth = 4, birthYear = 1978,
            professionDate = "05 July 2018",
            ordinationDate = "08 January 2022",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Parroquia de San Lorenzo",
            address = "Calle Benito Juárez Sur #316, 75855 Tehuacán, Puebla",
            cellPhone = "+52 23 8303 1994",
            email = "tonyezperanza1@gmail.com",
            rolesOrMinistries = "Párroco Parroquia de San Lorenzo Teotipilco (2023)"
        ),
        BasilianDirectoryMember(
            id = "inglis_brian",
            name = "Inglis, Brian Desmond",
            birthDateRaw = "24 August 1932",
            birthDay = 24, birthMonth = 8, birthYear = 1932,
            professionDate = "15 August 1952",
            ordinationDate = "29 June 1961",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "155 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 155",
            email = "inglis@basilian.org",
            rolesOrMinistries = "Hermano Nonagenario (92 años)"
        ),
        BasilianDirectoryMember(
            id = "janisse_wilfrid",
            name = "Janisse, Wilfrid Warren",
            birthDateRaw = "24 December 1929",
            birthDay = 24, birthMonth = 12, birthYear = 1929,
            professionDate = "15 August 1950",
            ordinationDate = "29 June 1958",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "151 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 151",
            email = "janisse@basilian.org",
            rolesOrMinistries = "Hermano Nonagenario (95 años)"
        ),
        BasilianDirectoryMember(
            id = "johnson_ted",
            name = "Johnson, Joseph Edward (Ted)",
            birthDateRaw = "11 October 1937",
            birthDay = 11, birthMonth = 10, birthYear = 1937,
            professionDate = "12 September 1960",
            ordinationDate = "09 December 1967",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "387 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            email = "tedjohnsoncsb@outlook.com"
        ),
        BasilianDirectoryMember(
            id = "johnson_quentin",
            name = "Johnson, Quentin Robert (Q.)",
            birthDateRaw = "04 August 1935",
            birthDay = 4, birthMonth = 8, birthYear = 1935,
            professionDate = "24 September 1960",
            ordinationDate = "10 December 1966",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Amica Riverside",
            address = "503 - 4909 Riverside Dr. E., Windsor, ON N8Y 0A4",
            cellPhone = "519-562-5355",
            email = "dominicrobertquentin@gmail.com",
            rolesOrMinistries = "Hermano Nonagenario (90 años)"
        ),
        BasilianDirectoryMember(
            id = "juarez_jose_alberto",
            name = "Juárez Sandoval, José Alberto",
            birthDateRaw = "30 August 1979",
            birthDay = 30, birthMonth = 8, birthYear = 1979,
            professionDate = "15 August 2012",
            ordinationDate = "05 January 2019",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Basilian Fathers of Houma",
            address = "100 Lumen Christi Court, Schriever, LA 70395",
            cellPhone = "+1 985-772-5422",
            email = "josealbertosandoval80@gmail.com",
            rolesOrMinistries = "Rector en Houma 2025 • Promotor de Justicia Latinoamérica"
        ),

        // =====================================================================
        // K
        // =====================================================================
        BasilianDirectoryMember(
            id = "kasun_robert",
            name = "Kasun, + Robert Michael",
            birthDateRaw = "20 December 1951",
            birthDay = 20, birthMonth = 12, birthYear = 1951,
            professionDate = "08 July 1973",
            ordinationDate = "24 June 1978",
            category = MemberCategory.BISHOP,
            country = "Canadá",
            currentHouse = "Blessed Sacrament Church",
            address = "24 Cheritan Ave., Toronto, ON M4R 1S4",
            cellPhone = "416-788-9387",
            email = "bkasun@basilian.org",
            rolesOrMinistries = "Obispo Auxiliar emérito de Toronto"
        ),
        BasilianDirectoryMember(
            id = "katulski_david",
            name = "Katulski, David William",
            birthDateRaw = "19 February 1945",
            birthDay = 19, birthMonth = 2, birthYear = 1945,
            professionDate = "22 December 1974",
            ordinationDate = "12 May 1979",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Curial Community",
            address = "16 Bernard Ave., Toronto, ON M5R 1R2",
            cellPhone = "647-449-2627",
            email = "katulski@basilian.org",
            rolesOrMinistries = "Gestor de Casos y Mediaciones de la Congregación"
        ),
        BasilianDirectoryMember(
            id = "kennedy_gordon",
            name = "Kennedy, Gordon Francis",
            birthDateRaw = "21 April 1936",
            birthDay = 21, birthMonth = 4, birthYear = 1936,
            professionDate = "15 August 1958",
            ordinationDate = "10 December 1966",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "153 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "416-709-0120",
            email = "gkndy21@hotmail.com"
        ),
        BasilianDirectoryMember(
            id = "kersch_terence",
            name = "Kersch, Terence Joseph",
            birthDateRaw = "11 December 1956",
            birthDay = 11, birthMonth = 12, birthYear = 1956,
            professionDate = "02 January 1997",
            ordinationDate = "29 June 2001",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House / St. Joseph’s Mission Center",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "281-662-1577",
            email = "tkersch@basilian.org",
            rolesOrMinistries = "Segundo Consultor General (2026-2030) • Director de St. Joseph’s Mission Center"
        ),
        BasilianDirectoryMember(
            id = "kirley_kevin",
            name = "Kirley, Kevin John",
            birthDateRaw = "29 July 1926",
            birthDay = 29, birthMonth = 7, birthYear = 1926,
            professionDate = "15 August 1945",
            ordinationDate = "29 June 1952",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "152 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            officePhone = "647-350-3755 ext. 152",
            email = "kkirley@basilian.org",
            rolesOrMinistries = "Decano de la Congregación (99 años) • 80 años de vida consagrada"
        ),
        BasilianDirectoryMember(
            id = "klein_david",
            name = "Klein, David Otto",
            birthDateRaw = "19 August 1942",
            birthDay = 19, birthMonth = 8, birthYear = 1942,
            professionDate = "15 August 1961",
            ordinationDate = "05 December 1970",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Las Cruces",
            address = "1682 Alta Vista Place, Las Cruces, NM 88011",
            cellPhone = "575-405-3469"
        ),

        // =====================================================================
        // L
        // =====================================================================
        BasilianDirectoryMember(
            id = "lehman_michael",
            name = "Lehman, Michael Alfred",
            birthDateRaw = "23 April 1952",
            birthDay = 23, birthMonth = 4, birthYear = 1952,
            professionDate = "07 August 1983",
            ordinationDate = "16 May 1987",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "University of St. Michael's College",
            address = "81 St. Mary St., Toronto, ON M5S 1J4",
            cellPhone = "416-201-2350",
            email = "mich52alf@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "leung_andrew",
            name = "Leung, Andrew Fook Bun",
            birthDateRaw = "27 September 1978",
            birthDay = 27, birthMonth = 9, birthYear = 1978,
            professionDate = "15 August 2004",
            ordinationDate = "19 April 2008",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Basilian Fathers of SMCS",
            address = "5 Oriole Parkway, Toronto, ON M4V 2C8",
            cellPhone = "647-215-5008",
            email = "leung@basilian.org",
            rolesOrMinistries = "Tesorero General de la Congregación (2024) • Presidente SMCS"
        ),
        BasilianDirectoryMember(
            id = "lococo_donald",
            name = "Lococo, Donald Joseph",
            birthDateRaw = "06 February 1953",
            birthDay = 6, birthMonth = 2, birthYear = 1953,
            professionDate = "10 August 1986",
            ordinationDate = "05 May 1990",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Windsor",
            address = "1158 Giles Blvd. E., Windsor, ON N9A 4G4",
            cellPhone = "226-344-7211",
            email = "donaldlococo@mac.com"
        ),

        // =====================================================================
        // M
        // =====================================================================
        BasilianDirectoryMember(
            id = "malo_john",
            name = "Malo, John Bernard",
            birthDateRaw = "14 September 1947",
            birthDay = 14, birthMonth = 9, birthYear = 1947,
            professionDate = "08 September 1967",
            ordinationDate = "06 May 1978",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "463 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "416-433-4066",
            email = "jbmcsb@yahoo.com",
            rolesOrMinistries = "Superior Presentation Manor (2024)"
        ),
        BasilianDirectoryMember(
            id = "mannara_kevin",
            name = "Mannara, Kevin Joseph",
            birthDateRaw = "16 August 1963",
            birthDay = 16, birthMonth = 8, birthYear = 1963,
            professionDate = "09 July 2014",
            ordinationDate = "05 August 2017",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Our Lady of the Assumption Parish",
            address = "350 Huron Church Rd., Windsor, ON N9C 2J9",
            cellPhone = "585-613-5488",
            email = "kmannara@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "martinez_julio",
            name = "Martínez Chávez, Julio César",
            birthDateRaw = "28 September 1988",
            birthDay = 28, birthMonth = 9, birthYear = 1988,
            professionDate = "17 July 2021",
            ordinationDate = "12 June 2025",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 55 3074 4220",
            email = "juliocemchavez@gmail.com",
            rolesOrMinistries = "Vicario Parroquia San Pedro de Verona, Mártir"
        ),
        BasilianDirectoryMember(
            id = "martinez_walter",
            name = "Martínez Royaceli, Walter Roberto",
            birthDateRaw = "06 October 1995",
            birthDay = 6, birthMonth = 10, birthYear = 1995,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 73 5191 2935",
            email = "waltermartz10@gmail.com",
            rolesOrMinistries = "Escolástico Basiliano"
        ),
        BasilianDirectoryMember(
            id = "may_william",
            name = "May, William Leslie",
            birthDateRaw = "19 November 1958",
            birthDay = 19, birthMonth = 11, birthYear = 1958,
            professionDate = "02 January 1996",
            ordinationDate = "29 June 2000",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Basilian Fathers of SMCS",
            address = "5 Oriole Parkway, Toronto, ON M4V 2C8",
            cellPhone = "416-580-0504",
            email = "may@basilian.org",
            rolesOrMinistries = "Rector SMCS (2018, 2021, 2025) • Director de Adquisición de Idiomas"
        ),
        BasilianDirectoryMember(
            id = "mcdonald_glenn",
            name = "McDonald, Glenn William",
            birthDateRaw = "20 July 1978",
            birthDay = 20, birthMonth = 7, birthYear = 1978,
            professionDate = "02 January 2003",
            ordinationDate = "19 April 2008",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Basilian Fathers of Edmonton",
            address = "11828 85th St., Edmonton, AB T5B 3E9",
            cellPhone = "780-668-9092",
            email = "glenn.mcdonaldcsb@gmail.com",
            rolesOrMinistries = "Párroco St. Alphonsus & St. Clare • Presidente Comité Finanzas"
        ),
        BasilianDirectoryMember(
            id = "mcgill_paul",
            name = "McGill, Paul Michael",
            birthDateRaw = "07 August 1942",
            birthDay = 7, birthMonth = 8, birthYear = 1942,
            professionDate = "01 September 1968",
            ordinationDate = "19 April 1975",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Amica Riverside",
            address = "402 - 4909 Riverside Dr. E., Windsor, ON N8Y 0A4",
            cellPhone = "519-300-3663",
            email = "mcgill@basilian.org"
        ),
        BasilianDirectoryMember(
            id = "mcleod_donald",
            name = "McLeod, Donald John",
            birthDateRaw = "26 September 1943",
            birthDay = 26, birthMonth = 9, birthYear = 1943,
            professionDate = "15 August 1962",
            ordinationDate = "06 May 1978",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "197 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "780-289-3583",
            email = "djmcleod43@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "miller_john_michael",
            name = "Miller, + John Michael",
            birthDateRaw = "09 July 1946",
            birthDay = 9, birthMonth = 7, birthYear = 1946,
            professionDate = "12 September 1966",
            ordinationDate = "29 June 1975",
            category = MemberCategory.BISHOP,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "832-420-1863",
            email = "mmiller@rcav.org",
            rolesOrMinistries = "Arzobispo Emérito de Vancouver • Superior Keon House (2025)"
        ),
        BasilianDirectoryMember(
            id = "moffatt_joseph",
            name = "Moffatt, Joseph Jerome",
            birthDateRaw = "05 February 1933",
            birthDay = 5, birthMonth = 2, birthYear = 1933,
            professionDate = "15 August 1952",
            ordinationDate = "29 June 1961",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Northville",
            address = "18732 Jamestown Circle, Northville, MI 48168",
            cellPhone = "248-499-0942",
            email = "jmof515@gmail.com",
            rolesOrMinistries = "Hermano Nonagenario (92 años)"
        ),
        BasilianDirectoryMember(
            id = "mora_pedro_miguel",
            name = "Mora Medina, Pedro Miguel",
            birthDateRaw = "10 June 1971",
            birthDay = 10, birthMonth = 6, birthYear = 1971,
            professionDate = "02 January 2000",
            ordinationDate = "02 January 2004",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Parroquia Nuestra Señora de Egipto",
            address = "Cra. 4 Este #10-02, Barrio Egipto, Bogotá",
            cellPhone = "+57 312 734 1777",
            email = "pedromiguel121@yahoo.com",
            rolesOrMinistries = "Tercer Consejero General • Párroco Nuestra Señora de Egipto (2023)"
        ),
        BasilianDirectoryMember(
            id = "morfin_francois",
            name = "Morfin, François Jean",
            birthDateRaw = "22 June 1946",
            birthDay = 22, birthMonth = 6, birthYear = 1946,
            professionDate = "12 September 1966",
            ordinationDate = "12 February 1977",
            category = MemberCategory.PRIEST,
            country = "Francia",
            currentHouse = "EHPAD Saint-Joseph à Varagnes",
            address = "51 Chemin de la Convalescence, 07100 Annonay, France",
            officePhone = "(011 33) 04 26 30 90 34",
            rolesOrMinistries = "Comunidad de Annonay"
        ),
        BasilianDirectoryMember(
            id = "mosey_douglas",
            name = "Mosey, Douglas Lorne",
            birthDateRaw = "04 September 1944",
            birthDay = 4, birthMonth = 9, birthYear = 1944,
            professionDate = "15 August 1963",
            ordinationDate = "11 May 1974",
            country = "USA",
            currentHouse = "Meriden",
            address = "51 Beverly Dr., Meriden, CT 06451",
            cellPhone = "860-478-3058",
            email = "frdmosey@gmail.com",
            category = MemberCategory.PRIEST
        ),
        BasilianDirectoryMember(
            id = "moslosky_robert",
            name = "Moslosky, Robert William",
            birthDateRaw = "19 June 1948",
            birthDay = 19, birthMonth = 6, birthYear = 1948,
            professionDate = "17 August 1980",
            ordinationDate = "08 May 1974",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Basilian Fathers of Novi",
            address = "47400 Heritage Dr. #207, Novi, MI 48374",
            cellPhone = "248-982-2948",
            email = "rwmoslosky@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "murphy_james",
            name = "Murphy, James Francis",
            birthDateRaw = "19 April 1972",
            birthDay = 19, birthMonth = 4, birthYear = 1972,
            professionDate = "02 January 1997",
            ordinationDate = "26 May 2001",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "281-702-1201",
            email = "murphcsb@yahoo.com",
            rolesOrMinistries = "Vicepresidente de Misión St. Thomas High School"
        ),

        // =====================================================================
        // N - O
        // =====================================================================
        BasilianDirectoryMember(
            id = "noelke_dennis",
            name = "Noelke, Dennis Paul",
            birthDateRaw = "29 July 1953",
            birthDay = 29, birthMonth = 7, birthYear = 1953,
            professionDate = "15 August 1976",
            ordinationDate = "09 May 1981",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Basilian Fathers of Novi",
            address = "47400 Heritage Dr. #250, Novi, MI 48374",
            cellPhone = "248-567-9937",
            email = "dpn@basilian.org",
            rolesOrMinistries = "Director de Vocaciones y Asociados USA/Canadá"
        ),
        BasilianDirectoryMember(
            id = "oconnor_paul",
            name = "O’Connor, Paul Francis",
            birthDateRaw = "17 July 1941",
            birthDay = 17, birthMonth = 7, birthYear = 1941,
            professionDate = "15 August 1960",
            ordinationDate = "12 December 1970",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "713-204-7383",
            email = "paulfochap@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "oneill_james",
            name = "O’Neill, James Michael",
            birthDateRaw = "08 October 1954",
            birthDay = 8, birthMonth = 10, birthYear = 1954,
            professionDate = "15 August 1982",
            ordinationDate = "10 May 1986",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Our Lady of the Assumption Parish",
            address = "350 Huron Church Rd., Windsor, ON N9C 2J9",
            cellPhone = "226-260-1870",
            email = "frjimo52@gmail.com",
            rolesOrMinistries = "Director de Envejecimiento Saludable & Eldercare Canadá"
        ),

        // =====================================================================
        // P
        // =====================================================================
        BasilianDirectoryMember(
            id = "pilsner_joseph",
            name = "Pilsner, Joseph Eric",
            birthDateRaw = "31 August 1961",
            birthDay = 31, birthMonth = 8, birthYear = 1961,
            professionDate = "15 August 1987",
            ordinationDate = "11 May 1991",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "University of St. Michael’s College",
            address = "81 St. Mary St., Toronto, ON M5S 1J4",
            cellPhone = "281-716-0971",
            email = "pilsnerjoseph@gmail.com",
            rolesOrMinistries = "Director de Retiros del Superior General"
        ),
        BasilianDirectoryMember(
            id = "pinsonneault_bernard",
            name = "Pinsonneault, Bernard John",
            birthDateRaw = "03 November 1934",
            birthDay = 3, birthMonth = 11, birthYear = 1934,
            professionDate = "12 September 1956",
            ordinationDate = "11 December 1965",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Windsor",
            address = "210 - 1953 Cabana Rd. W., Windsor, ON N9G 2X6",
            cellPhone = "226-739-7656",
            email = "frbjcsb@pm.me",
            rolesOrMinistries = "Hermano Nonagenario (90 años)"
        ),
        BasilianDirectoryMember(
            id = "porter_charles",
            name = "Porter, Charles Daniel",
            birthDateRaw = "15 December 1944",
            birthDay = 15, birthMonth = 12, birthYear = 1944,
            professionDate = "01 September 1968",
            ordinationDate = "26 April 1975",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "832-961-6731",
            email = "porter.daniel15@yahoo.com"
        ),

        // =====================================================================
        // R
        // =====================================================================
        BasilianDirectoryMember(
            id = "ramirez_ricardo",
            name = "Ramírez, + Ricardo",
            birthDateRaw = "12 September 1936",
            birthDay = 12, birthMonth = 9, birthYear = 1936,
            professionDate = "12 September 1960",
            ordinationDate = "10 December 1966",
            category = MemberCategory.BISHOP,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "575-649-2087",
            email = "rramirez@rcdlc.org",
            rolesOrMinistries = "Primer Obispo de Las Cruces, NM (Emérito) • Pionero en México y Catequesis"
        ),
        BasilianDirectoryMember(
            id = "ramos_amaury",
            name = "Ramos Romero, Amaury Rafael",
            birthDateRaw = "06 June 1984",
            birthDay = 6, birthMonth = 6, birthYear = 1984,
            professionDate = "02 January 2024",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 315 288 0052",
            email = "amaury.ramos@upb.edu.co",
            rolesOrMinistries = "Escolástico Basiliano en Bogotá"
        ),
        BasilianDirectoryMember(
            id = "reddy_john",
            name = "Reddy, John Martin",
            birthDateRaw = "13 August 1950",
            birthDay = 13, birthMonth = 8, birthYear = 1950,
            professionDate = "10 August 1986",
            ordinationDate = "05 June 1981",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "461 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "647-779-9428",
            email = "jmrcsb@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "reilly_alexander",
            name = "Reilly, Alexander Leo",
            birthDateRaw = "30 April 1935",
            birthDay = 30, birthMonth = 4, birthYear = 1935,
            professionDate = "12 September 1953",
            ordinationDate = "16 December 1962",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "145 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "647-717-2430",
            email = "lereilly1@hotmail.com",
            rolesOrMinistries = "Hermano Nonagenario (90 años)"
        ),
        BasilianDirectoryMember(
            id = "rennick_paul",
            name = "Rennick, Paul Joseph",
            birthDateRaw = "27 August 1949",
            birthDay = 27, birthMonth = 8, birthYear = 1949,
            professionDate = "17 August 1975",
            ordinationDate = "02 June 1979",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Amica Riverside",
            address = "1107 - 4909 Riverside Dr. E., Windsor, ON N8Y 0A4",
            cellPhone = "519-562-6207",
            email = "prennick@cogeco.ca"
        ),
        BasilianDirectoryMember(
            id = "restivo_maurice",
            name = "Restivo, Maurice Luke",
            birthDateRaw = "05 March 1955",
            birthDay = 5, birthMonth = 3, birthYear = 1955,
            professionDate = "14 August 1983",
            ordinationDate = "27 December 1986",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "346-266-6152",
            email = "mrestivo@basilian.org",
            rolesOrMinistries = "Vicario General de la Congregación de San Basilio • Primer Consultor General (2026-2030)"
        ),
        BasilianDirectoryMember(
            id = "rice_morgan",
            name = "Rice, Morgan Vernon",
            birthDateRaw = "01 June 1974",
            birthDay = 1, birthMonth = 6, birthYear = 1974,
            professionDate = "14 August 2005",
            ordinationDate = "21 November 2009",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Curial Community / St. Basil’s Church",
            address = "16 Bernard Ave. / 50 St. Joseph St., Toronto, ON",
            cellPhone = "647-274-3807",
            email = "mrice@basilian.org",
            rolesOrMinistries = "Cuarto Consejero General (2026-2030) • Párroco St. Basil’s Church"
        ),
        BasilianDirectoryMember(
            id = "rivera_eduardo",
            name = "Rivera, Eduardo Iván",
            birthDateRaw = "14 May 1992",
            birthDay = 14, birthMonth = 5, birthYear = 1992,
            professionDate = "01 August 2015",
            ordinationDate = "23 May 2020",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Keon House",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "480-440-8544",
            email = "riverae1@stthom.edu",
            rolesOrMinistries = "Presidente Comisión Capítulo General 2026 • Universidad St. Thomas"
        ),
        BasilianDirectoryMember(
            id = "rodriguez_luis_alberto",
            name = "Rodríguez Justo, Luis Alberto",
            birthDateRaw = "10 December 1981",
            birthDay = 10, birthMonth = 12, birthYear = 1981,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 72 2465 6087",
            email = "albertrodri644@gmail.com",
            rolesOrMinistries = "Escolástico Basiliano"
        ),
        BasilianDirectoryMember(
            id = "rodriguez_carlos_arturo",
            name = "Rodríguez Marciales, Carlos Arturo",
            birthDateRaw = "17 March 1984",
            birthDay = 17, birthMonth = 3, birthYear = 1984,
            professionDate = "02 January 2013",
            ordinationDate = "02 January 2020",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Parroquia Nuestra Señora de Egipto",
            address = "Cra. 4 Este #10-02, Barrio Egipto, Bogotá",
            cellPhone = "+57 311 616 7854",
            email = "carlosarturo_04@hotmail.com",
            rolesOrMinistries = "Tesorero en Colombia (2024) • Vicario Parroquia de Egipto"
        ),
        BasilianDirectoryMember(
            id = "rodriguez_jose_de_jesus",
            name = "Rodríguez Pérez, José de Jesús",
            birthDateRaw = "14 August 1993",
            birthDay = 14, birthMonth = 8, birthYear = 1993,
            professionDate = "18 July 2020",
            ordinationDate = "12 June 2025",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Casa San Felipe",
            address = "Lázaro Cárdenas #715, 75855 Tehuacán, Puebla",
            cellPhone = "+52 55 6136 8561",
            email = "jose-de-jesus01@hotmail.com",
            rolesOrMinistries = "Tesorero en México (2026) • Promotor Vocacional México"
        ),
        BasilianDirectoryMember(
            id = "rojas_roberto",
            name = "Rojas Peña, Roberto",
            birthDateRaw = "30 June 1963",
            birthDay = 30, birthMonth = 6, birthYear = 1963,
            professionDate = "16 July 1992",
            ordinationDate = "03 January 1998",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Parroquia San Pedro de Verona, Mártir / Tlalmille",
            address = "Calle Enseñanza, 14650 Tlalpan, CDMX",
            cellPhone = "+52 23 8100 1002",
            email = "betocsb@hotmail.com",
            rolesOrMinistries = "Administrador Parroquial San Pedro Mártir • Rector Santa María de Guadalupe"
        ),
        BasilianDirectoryMember(
            id = "rojas_juan_carlos",
            name = "Rojas Ramos, Juan Carlos",
            birthDateRaw = "13 May 1977",
            birthDay = 13, birthMonth = 5, birthYear = 1977,
            professionDate = "02 January 2000",
            ordinationDate = "02 January 2006",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 313 678 0859",
            email = "humanidadnueva@gmail.com",
            rolesOrMinistries = "Superior en Colombia (2023) • Director de Escolásticos"
        ),
        BasilianDirectoryMember(
            id = "rosica_thomas",
            name = "Rosica, Thomas Michael",
            birthDateRaw = "03 March 1959",
            birthDay = 3, birthMonth = 3, birthYear = 1959,
            professionDate = "15 August 1982",
            ordinationDate = "19 April 1986",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Presentation Manor",
            address = "465 - 61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            cellPhone = "416-879-5766",
            email = "trosica@basilian.org",
            rolesOrMinistries = "Director de Formación Continua"
        ),
        BasilianDirectoryMember(
            id = "roussel_joseph",
            name = "Roussel Jr., Joseph Premilas",
            birthDateRaw = "05 January 1961",
            birthDay = 5, birthMonth = 1, birthYear = 1961,
            professionDate = "11 August 1985",
            ordinationDate = "01 June 1991",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Dominic Village",
            address = "2401 Holcombe Blvd., Houston, TX 77021",
            cellPhone = "346-425-1853",
            email = "josephjre@aol.com"
        ),

        // =====================================================================
        // S
        // =====================================================================
        BasilianDirectoryMember(
            id = "schmidt_warren",
            name = "Schmidt, Warren Roger",
            birthDateRaw = "09 May 1981",
            birthDay = 9, birthMonth = 5, birthYear = 1981,
            professionDate = "15 August 2009",
            ordinationDate = "10 May 2014",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Basilian Fathers of Edmonton",
            address = "11828 85th St., Edmonton, AB T5B 3E9",
            cellPhone = "780-655-7636",
            email = "81schmidt@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "schwenzer_ronald",
            name = "Schwenzer, Ronald George",
            birthDateRaw = "07 December 1939",
            birthDay = 7, birthMonth = 12, birthYear = 1939,
            professionDate = "15 August 1958",
            ordinationDate = "14 December 1968",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "281-330-9230",
            email = "rschwenzer@basilian.org"
        ),
        BasilianDirectoryMember(
            id = "scott_timothy",
            name = "Scott, Timothy McHugh",
            birthDateRaw = "22 October 1957",
            birthDay = 22, birthMonth = 10, birthYear = 1957,
            professionDate = "12 August 1979",
            ordinationDate = "04 May 1984",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Our Lady of the Assumption Parish",
            address = "350 Huron Church Rd., Windsor, ON N9C 2J9",
            cellPhone = "514-713-9546",
            email = "tmscsb@gmail.com",
            rolesOrMinistries = "Párroco Assumption Parish Windsor (2024)"
        ),
        BasilianDirectoryMember(
            id = "sharp_david",
            name = "Sharp, David Louis Gerard",
            birthDateRaw = "04 May 1941",
            birthDay = 4, birthMonth = 5, birthYear = 1941,
            professionDate = "15 August 1960",
            ordinationDate = "05 December 1970",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Wickenburg",
            address = "16300 Wagon Box, Wickenburg, AZ 85390",
            officePhone = "623-910-7847",
            email = "padredavidsharp@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "sierra_marco",
            name = "Sierra García, Marco Antonio",
            birthDateRaw = "04 March 1992",
            birthDay = 4, birthMonth = 3, birthYear = 1992,
            professionDate = "07 January 2023",
            category = MemberCategory.SCHOLASTIC,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 31 2150 2807",
            email = "ocram_sierra19@hotmail.com",
            rolesOrMinistries = "Escolástico Basiliano"
        ),
        BasilianDirectoryMember(
            id = "smith_george",
            name = "Smith, George Terence",
            birthDateRaw = "30 October 1960",
            birthDay = 30, birthMonth = 10, birthYear = 1960,
            professionDate = "10 August 1986",
            ordinationDate = "16 September 1989",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. John Fisher University",
            address = "3497 East Ave., Rochester, NY 14618",
            cellPhone = "585-261-6902",
            email = "gsmith@basilian.org",
            rolesOrMinistries = "Superior General Emérito (2010-2018)"
        ),
        BasilianDirectoryMember(
            id = "smith_thomas_allan",
            name = "Smith, Thomas Allan",
            birthDateRaw = "07 May 1953",
            birthDay = 7, birthMonth = 5, birthYear = 1953,
            professionDate = "12 August 1979",
            ordinationDate = "07 May 1983",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "University of St. Michael’s College",
            address = "81 St. Mary St., Toronto, ON M5S 1J4",
            cellPhone = "416-836-9498",
            email = "allan.smith@utoronto.ca",
            rolesOrMinistries = "Rector USMC (2017, 2020, 2023)"
        ),
        BasilianDirectoryMember(
            id = "stenberg_jim",
            name = "Stenberg, Jim Gordon",
            birthDateRaw = "10 April 1970",
            birthDay = 10, birthMonth = 4, birthYear = 1970,
            professionDate = "02 January 1996",
            ordinationDate = "16 March 2001",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Basilian Fathers of Edmonton",
            address = "11828 85th St., Edmonton, AB T5B 3E9",
            cellPhone = "780-221-6524",
            email = "jim.stenberg@gmail.com",
            rolesOrMinistries = "Rector de Edmonton (2022, 2025)"
        ),
        BasilianDirectoryMember(
            id = "storey_kevin",
            name = "Storey, Kevin Jude",
            birthDateRaw = "11 March 1964",
            birthDay = 11, birthMonth = 3, birthYear = 1964,
            professionDate = "14 August 1988",
            ordinationDate = "27 June 1992",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Curial Office & Community",
            address = "146 Davenport Road, Toronto, ON M5R 1J2",
            cellPhone = "647-554-2133",
            officePhone = "416-921-6674 ext. 203",
            email = "kstorey@basilian.org",
            rolesOrMinistries = "Ex Superior General (2018-2026) • Miembro del Consejo General"
        ),
        BasilianDirectoryMember(
            id = "suarez_german",
            name = "Suárez García, Germán Darío",
            birthDateRaw = "19 December 1978",
            birthDay = 19, birthMonth = 12, birthYear = 1978,
            professionDate = "02 January 2024",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 312 370 7489",
            email = "german_dario_suarez@hotmail.es",
            rolesOrMinistries = "Escolástico Basiliano en Bogotá"
        ),

        // =====================================================================
        // T - U
        // =====================================================================
        BasilianDirectoryMember(
            id = "tanck_norman",
            name = "Tanck, Norman Charles",
            birthDateRaw = "17 December 1944",
            birthDay = 17, birthMonth = 12, birthYear = 1944,
            professionDate = "01 September 1968",
            ordinationDate = "20 April 1974",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Frassati House",
            address = "124 Sackville St., Toronto, ON M5A 3E7",
            cellPhone = "416-931-6631",
            email = "ntanck@basilian.org",
            rolesOrMinistries = "Rector Frassati House (2014, 2017, 2020, 2023)"
        ),
        BasilianDirectoryMember(
            id = "thompson_jefferson",
            name = "Thompson, Jefferson Mark",
            birthDateRaw = "18 September 1960",
            birthDay = 18, birthMonth = 9, birthYear = 1960,
            professionDate = "15 August 1987",
            ordinationDate = "03 August 1991",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Curial Office",
            address = "146 Davenport Rd., Toronto, ON M5R 1J2",
            cellPhone = "416-882-0310",
            officePhone = "416-921-6674 ext. 207",
            email = "jthompson@basilian.org",
            rolesOrMinistries = "Secretario General de la Congregación (2024)"
        ),
        BasilianDirectoryMember(
            id = "usuga_anderson",
            name = "Úsuga Giraldo, Anderson Fabián",
            birthDateRaw = "03 April 1992",
            birthDay = 3, birthMonth = 4, birthYear = 1992,
            professionDate = "02 January 2016",
            ordinationDate = "23 November 2024",
            category = MemberCategory.PRIEST,
            country = "Colombia",
            currentHouse = "Parroquia San Basilio",
            address = "Calle 58 #102-05, Barrio Olaya Herrera, Medellín",
            cellPhone = "+57 320 407 9870",
            email = "andersonusuga3@hotmail.com",
            rolesOrMinistries = "Sacerdote Basiliano • Parroquia San Basilio Medellín"
        ),

        // =====================================================================
        // V
        // =====================================================================
        BasilianDirectoryMember(
            id = "vivas_juan_angel",
            name = "Vivas García, Juan Ángel",
            birthDateRaw = "14 January 1991",
            birthDay = 14, birthMonth = 1, birthYear = 1991,
            professionDate = "18 July 2020",
            category = MemberCategory.DEACON,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 55 8181 1454",
            email = "juan.angel.viga.csb@gmail.com",
            rolesOrMinistries = "Diácono • Próxima Ordenación Sacerdotal en CDMX"
        ),

        // =====================================================================
        // W - Z
        // =====================================================================
        BasilianDirectoryMember(
            id = "wahl_richard",
            name = "Wahl, Richard Allan",
            birthDateRaw = "10 November 1943",
            birthDay = 10, birthMonth = 11, birthYear = 1943,
            professionDate = "15 August 1962",
            ordinationDate = "26 May 1973",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "713-471-5939",
            email = "rawahlcsb@yahoo.com",
            rolesOrMinistries = "Superior Dillon House (2023)"
        ),
        BasilianDirectoryMember(
            id = "walsh_james_leo",
            name = "Walsh, James Leo",
            birthDateRaw = "11 April 1937",
            birthDay = 11, birthMonth = 4, birthYear = 1937,
            professionDate = "14 August 1977",
            ordinationDate = "29 June 1960",
            category = MemberCategory.PRIEST,
            country = "Canadá",
            currentHouse = "Amica Riverside",
            address = "707 - 4909 Riverside Dr. E., Windsor, ON N8Y 0A4",
            officePhone = "519-253-2493",
            email = "frjlwalsh@gmail.com"
        ),
        BasilianDirectoryMember(
            id = "walsh_paul_francis",
            name = "Walsh, Paul Francis",
            birthDateRaw = "29 January 1955",
            birthDay = 29, birthMonth = 1, birthYear = 1955,
            professionDate = "16 August 1981",
            ordinationDate = "25 April 1987",
            category = MemberCategory.PRIEST,
            country = "México",
            currentHouse = "Escolasticado Basiliano",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            cellPhone = "+52 23 8150 0534",
            email = "pfw@basilian.org",
            rolesOrMinistries = "Superior en México (2018, 2022) • Director de Escolásticos"
        ),
        BasilianDirectoryMember(
            id = "werbylo_walter",
            name = "Werbylo, Walter Michael",
            birthDateRaw = "09 December 1954",
            birthDay = 9, birthMonth = 12, birthYear = 1954,
            professionDate = "10 August 1986",
            ordinationDate = "14 July 1990",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "Dillon House",
            address = "1302 Kipling St., Houston, TX 77006",
            cellPhone = "248-982-2335",
            email = "werbylo@basilian.org",
            rolesOrMinistries = "Director de Envejecimiento Saludable & Eldercare USA"
        ),
        BasilianDirectoryMember(
            id = "zapalac_david",
            name = "Zapalac, David Joseph",
            birthDateRaw = "21 October 1963",
            birthDay = 21, birthMonth = 10, birthYear = 1963,
            professionDate = "13 August 1989",
            ordinationDate = "26 June 1993",
            category = MemberCategory.PRIEST,
            country = "USA",
            currentHouse = "St. Anne’s Parish",
            address = "2140 Westheimer Rd., Houston, TX 77098",
            cellPhone = "281-797-5061",
            officePhone = "713-526-3276",
            email = "davezapzap@yahoo.com",
            rolesOrMinistries = "Rector / Párroco St. Anne’s Parish (2020, 2023)"
        ),

        // =====================================================================
        // ESCOLÁSTICOS BASILIANOS RECIENTES (PRIMEROS VOTOS RELIGIOSOS 2026)
        // =====================================================================
        BasilianDirectoryMember(
            id = "cardenas_ciro",
            name = "Cárdenas Balaguera, Ciro Alfonso",
            birthDateRaw = "24 August 1993",
            birthDay = 24, birthMonth = 8, birthYear = 1993,
            professionDate = "08 August 2026 (Primera Profesión de Votos)",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa de Formación / Teologado",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 322 827 6426",
            email = "cardenasciroalfonso4@gmail.com",
            rolesOrMinistries = "Escolástico Basiliano en Formación Teológica (Colombia)"
        ),
        BasilianDirectoryMember(
            id = "mwehu_fabien",
            name = "Mwehu Itondo, Fabien",
            birthDateRaw = "17 January 1992",
            birthDay = 17, birthMonth = 1, birthYear = 1992,
            professionDate = "08 August 2026 (Primera Profesión de Votos)",
            category = MemberCategory.SCHOLASTIC,
            country = "Colombia",
            currentHouse = "Casa de Formación / Teologado",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+52 56 2797 7771",
            email = "fabienitondo47@gmail.com",
            rolesOrMinistries = "Escolástico Basiliano en Formación Teológica (México/Colombia)"
        ),

        // =====================================================================
        // NOVICIOS BASILIANOS (NOVICIADO CANÓNICO 2026-2027 - CASA ANNONAY)
        // =====================================================================
        BasilianDirectoryMember(
            id = "martinez_diego",
            name = "Martínez López, Diego Alejandro",
            birthDateRaw = "23 November 2000",
            birthDay = 23, birthMonth = 11, birthYear = 2000,
            professionDate = "Noviciado Canónico 2026-2027",
            category = MemberCategory.NOVICE,
            country = "Colombia",
            currentHouse = "Noviciado Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+52 23 8199 4543",
            email = "diego23.marlopez@gmail.com",
            rolesOrMinistries = "Novicio Basiliano (México/Colombia)"
        ),
        BasilianDirectoryMember(
            id = "villa_jose_yonni",
            name = "Villa de Jesús, José Yonni",
            birthDateRaw = "19 March 2002",
            birthDay = 19, birthMonth = 3, birthYear = 2002,
            professionDate = "Noviciado Canónico 2026-2027",
            category = MemberCategory.NOVICE,
            country = "Colombia",
            currentHouse = "Noviciado Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+52 23 8238 1922",
            email = "villadejesusj@gmail.com",
            rolesOrMinistries = "Novicio Basiliano (México/Colombia)"
        ),
        BasilianDirectoryMember(
            id = "rodriguez_jorge_mario",
            name = "Rodríguez Peña, Jorge Mario",
            birthDateRaw = "04 April 1999",
            birthDay = 4, birthMonth = 4, birthYear = 1999,
            professionDate = "Noviciado Canónico 2026-2027",
            category = MemberCategory.NOVICE,
            country = "Colombia",
            currentHouse = "Noviciado Casa Annonay",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            cellPhone = "+57 300 340 5036",
            email = "jorgerpmario44@gmail.com",
            rolesOrMinistries = "Novicio Basiliano (Colombia)"
        ),

        // =====================================================================
        // ASOCIADOS BASILIANOS Y COOPERADORES
        // =====================================================================
        BasilianDirectoryMember(
            id = "fernandez_aron",
            name = "Fernandez, Aron",
            birthDateRaw = "15 May 1988",
            birthDay = 15, birthMonth = 5, birthYear = 1988,
            professionDate = "01 June 2021",
            category = MemberCategory.ASSOCIATE,
            country = "USA",
            currentHouse = "St. Joseph's Mission Center",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            cellPhone = "713-525-3500",
            email = "afernandez@basilian.org",
            rolesOrMinistries = "Coordinador de Proyectos y Misiones • St. Joseph's Mission Center (Directorio 2026-2027)"
        )
    )

    // =========================================================================
    // EN MEMORIA Y AMOR FRATERNO: HERMANOS DIFUNTOS (NECROLOGÍA COMPLETA 1822-2026)
    // =========================================================================
    private val featuredDeceasedBrothers: List<DeceasedBrother> = listOf(
        // Recientes (2025 - 2026) según el Boletín Basiliano Vol 67 #4
        DeceasedBrother(
            id = "dec_carruthers",
            name = "P. James Carruthers, CSB",
            age = 90,
            birthAndDeathYears = "1935 - 2025",
            datePassed = "8 de agosto de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano en Canadá",
            memorialQuote = "Fiel testigo de la bondad y dedicación evangélica en la educación cristiana de generaciones en Canadá."
        ),
        DeceasedBrother(
            id = "dec_owens",
            name = "P. Jess Owens, CSB",
            age = 86,
            birthAndDeathYears = "1939 - 2025",
            datePassed = "17 de agosto de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano",
            memorialQuote = "Vida consagrada al estudio, a la paciencia y a la formación de la juventud con el corazón de un verdadero padre."
        ),
        DeceasedBrother(
            id = "dec_hilmer",
            name = "P. Douglas Hilmer, CSB",
            age = 64,
            birthAndDeathYears = "1961 - 2025",
            datePassed = "6 de noviembre de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano",
            memorialQuote = "Hermano entrañable, generoso en el pastoreo parroquial, siempre dispuesto a tender la mano con alegría y cercanía fraterna."
        ),
        DeceasedBrother(
            id = "dec_walsh_joseph",
            name = "P. Joseph Walsh, CSB",
            age = 89,
            birthAndDeathYears = "1936 - 2025",
            datePassed = "6 de noviembre de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano",
            memorialQuote = "Diligente servidor del Evangelio, hombre de oración profunda y de silencio fecundo que supo amar la vida comunitaria."
        ),
        DeceasedBrother(
            id = "dec_canning",
            name = "P. Wilfred Canning, CSB",
            age = 98,
            birthAndDeathYears = "1927 - 2025",
            datePassed = "19 de noviembre de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano Nonagenario",
            memorialQuote = "Casi un siglo de vida colmado de gracia, fidelidad al carisma de San Basilio y alegría franciscana en su caminar sacerdotal."
        ),
        DeceasedBrother(
            id = "dec_wheeler",
            name = "P. John Wheeler, CSB",
            age = 86,
            birthAndDeathYears = "1939 - 2025",
            datePassed = "9 de diciembre de 2025",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote Basiliano",
            memorialQuote = "Afabilidad, sabiduría académica y un corazón agradecido por la herencia recibida de los fundadores de Annonay."
        ),

        // Misión Colombia y Memoria Querida
        DeceasedBrother(
            id = "dec_thompson_vicente",
            name = "P. Vicente Thompson, CSB",
            age = 94,
            birthAndDeathYears = "1922 - 2016",
            datePassed = "2016",
            category = DeceasedCategory.COLOMBIA_MEMORIA,
            titleOrRole = "Misionero en Colombia durante 12 años",
            memorialQuote = "Dedicó doce años de su vida entre los colombianos dando testimonio ejemplar de la presencia y del amor entrañable de Jesucristo."
        ),
        DeceasedBrother(
            id = "dec_platt_felipe",
            name = "P. Felipe Wallace Platt, CSB",
            birthAndDeathYears = "Tradición Misionera",
            datePassed = "En la Gloria de Dios",
            category = DeceasedCategory.COLOMBIA_MEMORIA,
            titleOrRole = "Autor de Triduos y Vademecum en Colombia",
            memorialQuote = "Con generosidad y dedicación redactó y legó las devociones, oraciones y triduos que hoy unen a la comunidad en Colombia."
        ),

        // 10 Padres Fundadores (1822)
        DeceasedBrother(
            id = "dec_lapierre",
            name = "P. Joseph Bovier Lapierre, CSB",
            age = 81,
            birthAndDeathYears = "1757 - 1838",
            datePassed = "16 de agosto de 1838",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Primer Superior General (1822-1838)",
            memorialQuote = "Hombre de prudencia heroica durante la Revolución Francesa. Fundó la escuela clandestina de Saint-Symphorien y guió a los primeros diez."
        ),
        DeceasedBrother(
            id = "dec_tourvieille",
            name = "P. Pierre Tourvieille, CSB",
            age = 79,
            birthAndDeathYears = "1780 - 1859",
            datePassed = "06 de agosto de 1859",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Segundo Superior General (1838-1859)",
            memorialQuote = "Gran matemático y educador. Impulsó la misión transatlántica enviando a los primeros Basilianos a Canadá en 1852."
        ),
        DeceasedBrother(
            id = "dec_tracol",
            name = "P. Julien Tracol, CSB",
            age = 89,
            birthAndDeathYears = "1796 - 1885",
            datePassed = "03 de junio de 1885",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Primer Historiador y Secretario General (37 años)",
            memorialQuote = "El último de la vieja guardia: modesto, sabio, custodio de la memoria y la historia sagrada de nuestros orígenes."
        ),
        DeceasedBrother(
            id = "dec_duret",
            name = "P. Jacques Vincent Duret, CSB",
            age = 79,
            birthAndDeathYears = "1762 - 1841",
            datePassed = "03 de junio de 1841",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador • Primer Asistente",
            memorialQuote = "Hombre de paz y prudencia absoluta. Cuidó de las vocaciones pobres y las hermanas de la Providencia custodiaron su corazón."
        ),
        DeceasedBrother(
            id = "dec_payan",
            name = "P. Augustin Payan, CSB",
            age = 76,
            birthAndDeathYears = "1771 - 1847",
            datePassed = "08 de abril de 1847",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador • Primer Maestro de Novicios",
            memorialQuote = "Descrito como la bondad personificada. Director espiritual y confesor incansable de jóvenes estudiantes y seminaristas."
        ),
        DeceasedBrother(
            id = "dec_polly",
            name = "P. Jean-Baptiste Polly, CSB",
            age = 74,
            birthAndDeathYears = "1772 - 1846",
            datePassed = "28 de marzo de 1846",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador • Segundo Asistente",
            memorialQuote = "Antiguo alcalde que arriesgó su vida protegiendo sacerdotes. Constitución de hierro, corazón cálido y profesor metodológico."
        ),
        DeceasedBrother(
            id = "dec_pages",
            name = "P. Jean-François Pagès, CSB",
            age = 68,
            birthAndDeathYears = "1793 - 1861",
            datePassed = "27 de agosto de 1861",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador",
            memorialQuote = "Un gigante con porte de monje. Hombre de rigurosa oración, ayuno y caridad desbordante con los más desfavorecidos."
        ),
        DeceasedBrother(
            id = "dec_martinesche",
            name = "P. Henri Martinesche, CSB",
            age = 82,
            birthAndDeathYears = "1797 - 1879",
            datePassed = "24 de febrero de 1879",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador • Apóstol de Vaudevant",
            memorialQuote = "Se privaba de lo necesario para alimentar y vestir a los pobres. En su parroquia fue amado como un padre y venerado como un santo."
        ),
        DeceasedBrother(
            id = "dec_vallon",
            name = "P. Jean Antoine Vallon, CSB",
            age = 65,
            birthAndDeathYears = "1775 - 1840",
            datePassed = "13 de agosto de 1840",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador",
            memorialQuote = "Ejemplo viviente de la regla religiosa y disciplina. Entregó su alma al pie del altar donde iba a celebrar la Santa Misa."
        ),
        DeceasedBrother(
            id = "dec_fayolle",
            name = "P. André Fayolle, CSB",
            age = 76,
            birthAndDeathYears = "1791 - 1867",
            datePassed = "27 de abril de 1867",
            category = DeceasedCategory.FUNDADORES_1822,
            titleOrRole = "Padre Fundador • Superior del Collège de Privas",
            memorialQuote = "Más un padre que un maestro para sus alumnos. Creó una verdadera familia en torno a la mesa y el altar."
        ),

        // Superiores Generales de Santa Memoria
        DeceasedBrother(
            id = "dec_actorie",
            name = "P. Julien Actorie, CSB",
            age = 61,
            birthAndDeathYears = "1803 - 1864",
            datePassed = "1864",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Tercer Superior General (1859-1864)",
            memorialQuote = "Custodió la expansión de las primeras casas y la vivencia fiel de las Constituciones."
        ),
        DeceasedBrother(
            id = "dec_soulerin",
            name = "P. Jean-Mathieu Soulerin, CSB",
            age = 72,
            birthAndDeathYears = "1807 - 1879",
            datePassed = "1879",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Cuarto Superior General (1865-1879) • Pionero en Toronto",
            memorialQuote = "Primer superior de St. Michael's College en Toronto en 1852. Tendió el puente entre Europa y el Nuevo Mundo."
        ),
        DeceasedBrother(
            id = "dec_carr_henry",
            name = "P. Henry Carr, CSB",
            age = 83,
            birthAndDeathYears = "1880 - 1963",
            datePassed = "1963",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Superior General de Toronto (1930-1942)",
            memorialQuote = "Visionario de la educación católica superior en Canadá. Integró los colegios católicos en universidades seculares."
        ),
        DeceasedBrother(
            id = "dec_flahiff_cardinal",
            name = "Cardenal George Bernard Flahiff, CSB",
            age = 84,
            birthAndDeathYears = "1905 - 1989",
            datePassed = "1989",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Superior General (1954-1961) • Cardenal Arzobispo de Winnipeg",
            memorialQuote = "Logró el Decreto de Reunificación de 1955 entre los Basilianos de Francia y Norteamérica. Padre conciliar del Vaticano II."
        ),
        DeceasedBrother(
            id = "dec_decker_kenneth",
            name = "P. Kenneth John Decker, CSB",
            age = 77,
            birthAndDeathYears = "1938 - 2015",
            datePassed = "2015",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Superior General (2002-2010)",
            memorialQuote = "Fundador de 'Becoming Neighbors' para migrantes y refugiados. Gran impulsor de la solidaridad y la justicia social."
        ),
        DeceasedBrother(
            id = "dec_smith_george",
            name = "P. George Smith, CSB",
            age = 75,
            birthAndDeathYears = "1942 - 2020",
            datePassed = "2020",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Superior General (2010-2018)",
            memorialQuote = "Líder fraterno, impulsor de la pastoral vocacional en Colombia y México, y custodio del carisma en el nuevo milenio."
        ),
        DeceasedBrother(
            id = "dec_vincent_charles",
            name = "P. Charles Vincent, CSB",
            age = 70,
            birthAndDeathYears = "1828 - 1898",
            datePassed = "1898",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Pionero y Provincial de Toronto",
            memorialQuote = "Primer novicio ordenado en Canadá. Consolidó el St. Michael's College y la formación de generaciones de sacerdotes."
        ),
        DeceasedBrother(
            id = "dec_oconnor_denis",
            name = "Mons. Denis O'Connor, CSB",
            age = 70,
            birthAndDeathYears = "1841 - 1911",
            datePassed = "1911",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Arzobispo de Toronto • Obispo de London (Ontario)",
            memorialQuote = "Primer obispo y arzobispo basiliano en América del Norte. Hombre de principios inquebrantables y gran celo pastoral."
        ),
        DeceasedBrother(
            id = "dec_charbonnel_armand",
            name = "Mons. Armand-François de Charbonnel",
            age = 89,
            birthAndDeathYears = "1802 - 1891",
            datePassed = "1891",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Obispo de Toronto • Protector de los Basilianos",
            memorialQuote = "Invitó a sus antiguos maestros de Annonay a fundar la misión de Toronto en 1852. Más tarde vistió el hábito capuchino en Francia."
        ),
        DeceasedBrother(
            id = "dec_mohan_thomas",
            name = "P. Thomas Mohan, CSB",
            age = 82,
            birthAndDeathYears = "1931 - 2013",
            datePassed = "2013",
            category = DeceasedCategory.COLOMBIA_MEMORIA,
            titleOrRole = "Rector y Misionero en Colombia",
            memorialQuote = "Apoyó incansablemente las misiones en Cali y Bogotá con carisma entrañable, generosidad y alegría sacerdotal."
        ),
        DeceasedBrother(
            id = "dec_macdonell_ronald",
            name = "P. Ronald MacDonell, CSB",
            age = 78,
            birthAndDeathYears = "1940 - 2018",
            datePassed = "2018",
            category = DeceasedCategory.COLOMBIA_MEMORIA,
            titleOrRole = "Misionero y Lingüista en América Latina",
            memorialQuote = "Dedicó su vida a las comunidades indígenas y a la evangelización inculturada en América Latina con profundo amor evangélico."
        ),
        DeceasedBrother(
            id = "dec_coughlin_leo",
            name = "P. Leo Coughlin, CSB",
            age = 85,
            birthAndDeathYears = "1928 - 2013",
            datePassed = "2013",
            category = DeceasedCategory.COLOMBIA_MEMORIA,
            titleOrRole = "Formador de Misioneros",
            memorialQuote = "Acompañó con paciencia y sabiduría a los primeros sacerdotes basilianos colombianos en su camino formativo."
        ),
        DeceasedBrother(
            id = "dec_phelan_george",
            name = "P. George B. Phelan, CSB",
            age = 69,
            birthAndDeathYears = "1892 - 1961",
            datePassed = "1961",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Cofundador del Pontifical Institute of Mediaeval Studies (PIMS)",
            memorialQuote = "Ilustre filósofo y teólogo tomista. Llevó el pensamiento de San Basilio y Santo Tomás a la cúspide académica mundial."
        ),
        DeceasedBrother(
            id = "dec_mcconica_james",
            name = "P. James McConica, CSB",
            age = 91,
            birthAndDeathYears = "1930 - 2021",
            datePassed = "2021",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Historiador de Oxford y Presidente de PIMS",
            memorialQuote = "Caballero de la Orden de Canadá y erudito erasmiano. Unió la más alta ciencia con la piedad y la humildad sacerdotal."
        ),
        DeceasedBrother(
            id = "dec_irwin_william",
            name = "P. William Irwin, CSB",
            age = 69,
            birthAndDeathYears = "1928 - 1997",
            datePassed = "1997",
            category = DeceasedCategory.SUPERIORES_GENERALES,
            titleOrRole = "Fundador de Catholic Social Services",
            memorialQuote = "Inspirado en la Basiliada de San Basilio, fundó obras de acogida para personas sin hogar, mujeres y refugiados en Edmonton."
        ),
        DeceasedBrother(
            id = "dec_holmes_norman",
            name = "P. Norman Holmes, CSB",
            age = 88,
            birthAndDeathYears = "1934 - 2022",
            datePassed = "2022",
            category = DeceasedCategory.RECENTES_2025_2026,
            titleOrRole = "Sacerdote y Educador",
            memorialQuote = "Fiel pastor en parroquias y colegios basilianos, recordado por su bondad paternal y sonrisa reconfortante."
        )
    )

    val deceasedBrothers: List<DeceasedBrother> by lazy {
        BasilianNecrologyCatalog.buildCompleteDeceasedBrothersList(featuredDeceasedBrothers)
    }

    fun getDeceasedByDate(month: Int, day: Int): List<DeceasedBrother> {
        return deceasedBrothers.filter { it.monthPassed == month && it.dayPassed == day }
    }

    fun getDeceasedByMonth(month: Int): List<DeceasedBrother> {
        return deceasedBrothers.filter { it.monthPassed == month }.sortedWith(compareBy({ it.dayPassed }, { it.name }))
    }

    fun getUpcomingBirthdays(currentMonth: Int, currentDay: Int, limit: Int = 5): List<BasilianDirectoryMember> {
        return members.sortedBy { it.daysUntilBirthday(currentMonth, currentDay) }.take(limit)
    }

    fun getTodayBirthdays(currentMonth: Int, currentDay: Int): List<BasilianDirectoryMember> {
        return members.filter { it.isBirthdayToday(currentMonth, currentDay) }
    }

    fun getMembersByMonth(month: Int): List<BasilianDirectoryMember> {
        return members.filter { it.birthMonth == month }.sortedBy { it.birthDay }
    }

    // =========================================================================
    // DIRECTORIO OFICIAL 2026-2027: GOBIERNO GENERAL & CURIA GENERALICIA
    // =========================================================================
    val generalCouncil2026: List<BasilianCouncilMember> = listOf(
        BasilianCouncilMember(
            id = "c_huber",
            roleTitle = "Superior General",
            memberName = "Muy Rev. P. John Brian Huber, CSB",
            location = "Curia Generalicia • Toronto, Canadá",
            email = "jhuber@basilian.org",
            phone = "416-921-6674 ext. 202",
            term = "2026 - presente (Capítulo General 2026)",
            description = "Elegido Superior General de la Congregación de San Basilio en el Capítulo General 2026."
        ),
        BasilianCouncilMember(
            id = "c_restivo",
            roleTitle = "Vicario General & 1.er Consejero",
            memberName = "Rev. P. Maurice Luke Restivo, CSB",
            location = "Casa Annonay • Bogotá, Colombia",
            email = "mrestivo@basilian.org",
            phone = "+57 346 266 6152",
            term = "2026 - 2030",
            description = "Primer Consejero General y Vicario. Custodio de la misión en Colombia y formación de novicios."
        ),
        BasilianCouncilMember(
            id = "c_kersch",
            roleTitle = "2.° Consejero General & Dir. Misiones",
            memberName = "Rev. P. Terence Joseph Kersch, CSB",
            location = "St. Joseph's Mission Center • Houston, TX, USA",
            email = "tkersch@basilian.org",
            phone = "281-662-1577",
            term = "2026 - 2030",
            description = "Director de St. Joseph’s Mission Center y promotor de la pastoral misionera internacional."
        ),
        BasilianCouncilMember(
            id = "c_english",
            roleTitle = "3.er Consejero General",
            memberName = "Rev. P. Paul Francis English, CSB",
            location = "St. Anne’s Parish • Houston, TX, USA",
            email = "penglish@basilian.org",
            phone = "585-754-4777",
            term = "2026 - 2030",
            description = "Consejero General para la formación inicial, vocaciones y pastoral educativa."
        ),
        BasilianCouncilMember(
            id = "c_rice",
            roleTitle = "4.° Consejero General",
            memberName = "Rev. P. Morgan Vernon Rice, CSB",
            location = "St. Basil’s Church • Toronto, Canadá",
            email = "mrice@basilian.org",
            phone = "647-274-3807",
            term = "2026 - 2030",
            description = "Consejero General y párroco de la histórica iglesia colegial de St. Basil en Toronto."
        ),
        BasilianCouncilMember(
            id = "c_storey",
            roleTitle = "Ex Superior General (2018-2026)",
            memberName = "Rev. P. Kevin Jude Storey, CSB",
            location = "Curia Generalicia • Toronto, Canadá",
            email = "kstorey@basilian.org",
            phone = "416-921-6674 ext. 203",
            term = "2018 - 2026",
            description = "Guió la Congregación durante dos mandatos (2018-2026) fortaleciendo la misión en América Latina."
        ),
        BasilianCouncilMember(
            id = "c_fernandez",
            roleTitle = "Coordinador de Proyectos Misioneros",
            memberName = "Aron Fernandez",
            location = "St. Joseph's Mission Center • Houston, TX, USA",
            email = "afernandez@basilian.org",
            phone = "713-525-3500",
            term = "Directorio 2026-2027",
            description = "Asociado Basiliano y coordinador de apoyo a las misiones en Colombia, México y proyectos caritativos."
        )
    )

    // =========================================================================
    // DIRECTORIO OFICIAL 2026-2027: CASAS PRINCIPALES Y CENTROS DE MISIÓN
    // =========================================================================
    val basilianHouses2026: List<BasilianHouseInfo> = listOf(
        BasilianHouseInfo(
            id = "house_curia",
            name = "Curia Generalicia Basiliana",
            country = "Canadá",
            city = "Toronto, ON",
            address = "146 Davenport Road, Toronto, ON M5R 1J2",
            phone = "416-921-6674",
            email = "curia@basilian.org",
            superiorOrRector = "Muy Rev. P. John Brian Huber, CSB (Superior General)",
            category = "Sede de Gobierno General"
        ),
        BasilianHouseInfo(
            id = "house_st_joseph_mission",
            name = "St. Joseph’s Mission Center",
            country = "USA",
            city = "Houston, TX",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            phone = "713-525-3500",
            email = "missions@basilian.org",
            superiorOrRector = "Rev. P. Terence Joseph Kersch, CSB / Aron Fernandez",
            category = "Centro Internacional de Misiones"
        ),
        BasilianHouseInfo(
            id = "house_casa_annonay_bogota",
            name = "Casa Annonay & Noviciado Internacional",
            country = "Colombia",
            city = "Bogotá, D.C.",
            address = "Cra. 3 Este #10A-17, Barrio Egipto, Bogotá",
            phone = "+57 346 266 6152",
            email = "annonaycolombia@basilian.org",
            superiorOrRector = "Rev. P. Maurice Restivo, CSB (Maestro de Novicios)",
            category = "Casa de Formación & Noviciado"
        ),
        BasilianHouseInfo(
            id = "house_parroquia_san_basilio_medellin",
            name = "Comunidad & Parroquia San Basilio",
            country = "Colombia",
            city = "Medellín, Antioquia",
            address = "Calle 58 #102-05, Barrio Olaya Herrera, Medellín",
            phone = "+57 4 421 2100",
            email = "sanbasiliomed@gmail.com",
            superiorOrRector = "Rev. P. Anderson Úsuga, CSB (Párroco)",
            category = "Parroquia & Pastoral Comunitaria"
        ),
        BasilianHouseInfo(
            id = "house_escolasticado_tlalpan",
            name = "Escolasticado Basiliano San Basilio",
            country = "México",
            city = "Tlalpan, CDMX",
            address = "Las Fuentes 34, Toriello Guerra, 14050 Tlalpan, CDMX",
            phone = "+52 55 5606 1822",
            email = "escolasticadomx@basilian.org",
            superiorOrRector = "Rev. P. Paul Francis Walsh, CSB",
            category = "Casa de Estudios Teológicos & Filosofía"
        ),
        BasilianHouseInfo(
            id = "house_keon_house",
            name = "Keon House",
            country = "USA",
            city = "Houston, TX",
            address = "4019 Yoakum Blvd., Houston, TX 77006",
            phone = "713-525-3500",
            email = "keonhouse@basilian.org",
            superiorOrRector = "Rev. P. Edward Baenziger, CSB",
            category = "Comunidad Residencial"
        ),
        BasilianHouseInfo(
            id = "house_presentation_manor",
            name = "Presentation Manor",
            country = "Canadá",
            city = "Scarborough, ON",
            address = "61 Fairfax Cres., Scarborough, ON M1L 1Z7",
            phone = "647-350-3755",
            email = "presentationmanor@basilian.org",
            superiorOrRector = "Rev. P. Robert Barringer, CSB",
            category = "Residencia de Cuidado & Ancianos"
        ),
        BasilianHouseInfo(
            id = "house_annonay_cuna",
            name = "Comunidad de Annonay (Cuna Fundacional 1822)",
            country = "Francia",
            city = "Annonay, Ardèche",
            address = "51 Chemin de la Convalescence, 07100 Annonay, France",
            phone = "+33 04 75 32 42 32",
            email = "annonay1822@basilian.org",
            superiorOrRector = "Rev. P. Bernard Buisson, CSB",
            category = "Santuario Histórico & Cuna Fundacional"
        )
    )
}
