package com.example.data.repository

import com.example.data.model.AppLanguage
import com.example.data.model.DeceasedBrother
import com.example.data.model.DeceasedCategory

/**
 * Catálogo Oficial Completo de la Necrología Basiliana (Edición Noviembre 2026)
 * «BASILIAN NECROLOGY • NECROLOGÍA BASILIANA • NÉCROLOGIE BASILIENNE»
 * «Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres»
 *
 * Contiene todos los hermanos difuntos de la Congregación de San Basilio (C.S.B.)
 * desde el 1 de enero hasta el 31 de diciembre (1822 - 2026).
 */
object BasilianNecrologyCatalog {

    data class RawNecrologyEntry(
        val month: Int,
        val day: Int,
        val rawName: String,
        val birthYear: Int,
        val deathYear: Int
    )

    private val rawEntriesText = """
1|1|Rev. Joseph Lineham Willett|1911-1958
1|1|Rev. Wilfrid Clarence Sharpe|1891-1976
1|2|R.P. Ernest Cauvin|1857-1925
1|2|Rev. William James Stoba|1920-2007
1|2|Rev. William Lawrence Moran|1920-2007
1|2|Rev. Joseph Paul Shannon|1920-2013
1|3|Rev. Frank Canning Harrison|1905-1991
1|3|Rev. John Sheehan Broussard|1919-2012
1|4|Rev. Frederick Wallace Cahill|1920-1983
1|5|R.P. Elie Roudil|1870-1939
1|5|Rev. John Francis Madden|1921-2003
1|5|Rev. Robert William Crooker|1925-2018
1|6|R.P. Joseph B. Malbos|1822-1885
1|7|R.P. Joseph Martin|1840-1906
1|7|Rev. Hugh James Haffey|1905-1975
1|8|Rev. Robert David Chisholm|1934-2008
1|8|Rev. Franklin Joseph Mizzi|1938-2023
1|10|Rev. Richard Sheehan|1920-2008
1|10|Rev. George Edward Beaune|1926-2016
1|10|Rev. Joseph Ian Boyd|1935-2024
1|11|R.P. Jules Verger|1864-1934
1|11|Rev. Dennis Cuvillier Foy|1930-2003
1|12|R.P. Antoine Epitalon|1873-1965
1|12|Rev. Carl Mitchell Allnoch|1908-1969
1|13|R.P. Séraphin Perbet|1868-1951
1|14|R.P. Georges Louis Reynouard|1921-1995
1|14|Rev. Lawrence Mark Janisse|1926-2010
1|15|Rev. John Joseph Fiore|1929-2022
1|16|Rev. John David Tallon|1938-1982
1|16|Rev. Daniel John Zorzi|1960-2009
1|17|R.P. Jean-Claude Savoye|1848-1929
1|17|Rev. Clifford Joseph Crowley|1916-1992
1|17|Rev. Paul McEvoy James|1930-2001
1|19|Rev. James Francis Wilson|1912-1997
1|20|R.P. Louis Marie Joseph Demeure|1833-1896
1|20|Rev. Vincent Bernard Reath|1869-1929
1|20|Rev. Fergus Joseph Sheehy|1906-1985
1|21|R.P. Auguste Vernède|1846-1881
1|21|Rev. Elliott Bernard Allen|1921-1981
1|21|Rev. Daniel Joseph Mulvihill|1915-1987
1|21|Rev. Norman Martin Iversen|1927-1993
1|22|T.R. Julien Giraud|1848-1931
1|22|Rev. Charles Albert Christopher|1932-2006
1|22|Rev. Robert James Klem|1928-2021
1|23|R.P. Eugène Durand|1855-1932
1|24|Rev. Robert William Finn|1919-1995
1|25|R.P. Jean Pierre Ranc|1816-1894
1|25|Rev. John Dennis Sheehy|1908-1967
1|25|Rev. Harold Charles Perry|1923-1978
1|25|Rev. Allen James Genca|1932-1994
1|27|Rev. Uldège Joseph Girard|1910-1966
1|27|Rev. Raymond Páramo|1937-2020
1|28|M. Auguste Vaschalde|1873-1898
1|28|Rev. Leonard Joseph Dolan|1899-1948
1|28|Rev. Leon George Hart|1925-1990
1|28|Rev. Arthur John McCarthy|1945-1991
1|28|R.P. José Isidro Castillo|1957-1996
1|29|Rev. Robert Emmett Lamb|1915-2000
1|30|Rev. Francis Andrew Rafferty|1879-1960
1|30|Rev. Thomas John McGuire|1891-1964
1|30|Rev. Viator Ignatius McIntyre|1904-1974
1|30|Rev. William Maximus Murphy|1917-2004
1|31|R.P. Adolphe Arthur Vaschalde|1871-1942
1|31|R.P. Jean Louis Soleilhac|1861-1948
1|31|Rev. Maurice Adrian Record|1913-1997
2|2|Rev. George Donal O'Gorman|1923-1996
2|3|R.P. Firmin Hilaire|1865-1893
2|3|Rev. John Aloysius Sullivan|1873-1913
2|3|Rev. James Edward Martin|1906-1992
2|4|Rev. John Francis Callaghan|1923-2015
2|5|Rev. Vincent Conrad Eckert|1915-1978
2|5|Rev. James Reginald O'Donnell|1907-1988
2|5|Rev. Robert Gerard Ritz|1924-2015
2|7|R.P. Antoine Goutte|1843-1901
2|7|R.P. Paul Lacoste|1868-1930
2|7|Rev. Cornelius J. Sheehan|1902-1939
2|7|Rev. George Raymond Phelan|1914-1965
2|7|Rev. Albert William Cylwicki|1932-2021
2|8|R.P. Jean Claude Chavanon|1816-1902
2|8|V.R. Thomas James Hanrahan|1926-2012
2|8|Rev. James Joseph Gaunt|1934-2025
2|9|Rev. Luke Renaud|1850-1925
2|9|R.P. Léon Guigon|1872-1941
2|9|Rev. Leo Joseph Adam|1914-1987
2|9|Rev. Matthew Patrick Sheedy|1913-1989
2|9|Rev. Russell Anthony Pendergast|1921-1997
2|10|Rev. John Paul Mallon|1900-1974
2|11|R.P. Lucien Pagès|1858-1928
2|11|Rev. Michael Joseph Oliver|1888-1977
2|11|Rev. John Patrick O'Meara|1914-2000
2|12|M. Alphonse Tourvieille|1864-1888
2|12|Rev. John Joseph Costello|1870-1906
2|12|Rev. William Joseph Roach|1875-1961
2|12|Rev. Edward William Allor|1890-1974
2|13|R.P. Xavier Mourgue|1852-1912
2|14|Rev. Leo Charles Campbell|1939-2008
2|14|Rev. Richard Anthony Ranalletti|1944-2014
2|15|R.P. Jean-Marie Godard|1846-1924
2|15|R.P. Lucien Jules Brot|1914-1996
2|15|Rev. Albert Lawrence Williams|1926-2011
2|15|Rev. Joseph Thomas Rolland Janisse|1921-2014
2|15|Rev. William Joseph Frankenberger|1943-2022
2|16|Rev. Robert Barr Hale|1937-2000
2|16|Rev. Thomas John Stokes|1926-2009
2|16|Rev. Robert Joseph Madden|1928-2013
2|17|Rev. Arthur Francis Waligore|1924-1977
2|17|Rev. Norman Joseph Fitzpatrick|1927-2015
2|17|Rev. Alvin Alexander Sinasac|1951-2022
2|18|Rev. Robert Oliver McKinnon|1925-1999
2|18|Rev. Matthew Thomas Mulcahy|1911-2000
2|18|Rev. Claude Graveley Arnold|1929-2018
2|19|Rev. Donald Thomas Faught|1915-1975
2|21|R.P. Hilarion Merle|1832-1913
2|21|Rev. John Thaddeus Cherry|1919-1973
2|21|Rev. Leonard Clarence Quinlan|1903-1983
2|22|Rev. Austin David O'Brien|1898-1949
2|22|Rev. David Esmonde Belyea|1927-2008
2|23|M. Auguste Clément Bodineau|1844-1875
2|24|R.P. Henri André Martinesche|1797-1879
2|24|Rev. John Bernard Collins|1853-1920
2|24|Mr. Thomas Walthall Leman|1942-1970
2|25|R.P. François Roux-Saget|1817-1890
2|25|R.P. Dominique Florentin Molin|1840-1921
2|25|Rev. Vincent Joseph Guinan|1899-1973
2|26|Rev. John Francis Murphy|1909-1975
2|26|Rev. Alfred Page Caird|1915-1980
2|27|Rev. James Francis Cross|1915-1976
2|28|M. Léon Mirabel|1875-1899
3|1|R.P. Léopold Chambon|1857-1919
3|1|V.R. James Frederic Player|1871-1931
3|1|Rev. James Scott Kelly|1924-1996
3|1|Rev. Donald Edmund Kuder|1934-2014
3|2|Rev. Michael Joseph Mungovan|1846-1901
3|2|R.P. François Xavier Granottier|1836-1917
3|2|Rev. James Edward Rent|1936-2013
3|3|Rev. Wilfrid Martin Kehoe|1909-1977
3|5|R.P. Jean-Marie Bret|1872-1908
3|5|Rev. Peter O'Donohue|1846-1915
3|5|R.P. Félix Séveyrac|1872-1932
3|5|Rev. Les Francis Schaefer|1932-2023
3|6|Rev. John Joseph Damato|1931-1985
3|7|Rev. Simon Andrew Perdue|1893-1966
3|9|Rev. Thomas Joseph Heydon|1857-1935
3|9|V.R. Edmund Joseph McCorkell|1891-1980
3|9|Rev. Frederick Arthur Black|1924-2000
3|9|Rev. Francis Henry Black|1927-2006
3|11|Rev. David Gordon Heath|1930-2007
3|11|Rev. Thomas William Sepúlveda|1944-2023
3|12|Rev. Daniel Vincent Burns|1906-1993
3|13|R.P. Joseph Frédéric Bonfils|1847-1918
3|15|R.P. Jacques Joseph Deglesne|1935-2005
3|15|Rev. Donald Eugene McCarthy|1929-2011
3|17|Rev. Norman Joseph Murphy|1905-1985
3|18|Rev. Cyril Francis Carter|1913-1971
3|18|Rev. Charles Wallace Leland|1928-2005
3|19|Rev. William James Brown|1915-1991
3|19|Rev. Simon Raphael O'Loughlin|1916-1998
3|20|Rev. J. J. Kenneth O'Keefe|1930-2018
3|21|M. Charles Hours|1851-1878
3|21|Rev. Thomas Patrick O'Rourke|1889-1955
3|21|Rev. Joseph Alfred O'Reilly|1917-1993
3|22|R.P. Victorin Charron|1866-1931
3|22|Rev. Emil Jerome Welty|1888-1962
3|22|Rev. Maurice Francis Whelan|1909-1990
3|22|Rev. Armand Augustine Maurer|1915-2008
3|23|Rev. George Frederick Freemesser|1933-2004
3|24|R.P. André Charmant|1802-1878
3|25|R.P. Denis Mouraret|1866-1938
3|25|Rev. Vincent Lorne Kennedy|1899-1974
3|25|Rev. Richard Joseph Jeffery|1924-1978
3|25|V.R. Ulysse Ernest Paré|1935-2014
3|26|Mr. Charles Francis Roe|1916-1939
3|26|Rev. James Thomas McGouey|1904-1969
3|26|Rev. Francis Austin Brown|1907-1977
3|27|Rev. Bernard Joseph O'Donnell|1899-1977
3|27|Rev. John Edward McManus|1927-2000
3|27|Rev. Clarence Ervin Watrin|1932-1924
3|28|R.P. Jean-Baptiste Polly|1772-1846
3|28|Rev. Patrick L. Buckley|1844-1913
3|28|R.P. Auguste Pouzol|1845-1915
3|28|R.P. Jean Fleury Vallanson|1841-1926
3|28|Rev. Vincent Joseph Murphy|1877-1933
3|28|Rev. John Francis Ware|1925-1995
3|28|Rev. Vincent Alton Paré|1927-1998
3|28|Rev. Joseph Allen Quinn|1933-2019
3|29|R.P. Marius Guey|1872-1900
3|29|R.P. Gabriel Fuma|1864-1932
3|29|Rev. John McDermott Wilson|1919-2018
3|30|R.P. Jean-Baptiste Soleilhac|1848-1920
3|31|R.P. Léopold Chanteperdrix|1861-1941
3|31|R.P. Joachim Rieubon|1871-1952
3|31|Rev. Richard Joseph Allard|1935-1994
4|1|Rev. Leonard Anthony Kennedy|1922-2010
4|2|Mr. John Richard Moffit|1853-1876
4|3|Rev. Robert Henry Flood|1919-1974
4|3|Rev. Gerald Telesphore Gregoire|1924-2005
4|3|Rev. Harold Vincent O'Leary|1920-2011
4|4|Rev. Thomas Frédéric Gignac|1868-1915
4|4|Rev. Harold Bernard Gardner|1929-2012
4|4|Rev. Donald Francis Finlay|1936-2025
4|5|R.P. Edouard Aureille|1862-1934
4|5|Rev. Wilfrid Francis Garvey|1899-1930
4|5|Rev. Mr. Thomas McCarthy|1841-1865
4|5|Rev. Thomas Vincent Moylan|1879-1942
4|6|R.P. Jean Monin|1829-1899
4|6|R.P. Henri Ladreyt|1833-1915
4|6|Rev. John Francis Boland|1916-1969
4|6|Rev. Eugene Adams Heyck|1921-1989
4|6|Rev. Rudolph Stephen Diemer|1906-1992
4|7|Rev. Thomas Vernon Kennedy|1907-1949
4|7|Rev. Francis Emmett Monaghan|1920-1995
4|8|R.P. Augustin Payan|1771-1847
4|8|R.P. Patrick Molony|1813-1880
4|8|Rev. James Vincent Burke|1899-1949
4|8|Rev. Edward Joseph McMahon|1906-1970
4|8|Rev. Florian Edmund Pacher|1914-1977
4|8|Rev. Basil Francis Glavin|1921-1985
4|9|Rev. Alfred Jacques Côté|1855-1933
4|10|Rev. Eugene Robert Malley|1924-1980
4|10|Rev. Thomas Dake Batty|1902-1986
4|10|Rev. David Linus Armstrong|1941-1992
4|10|Rev. Frank Hart Bredeweg|1928-2009
4|11|Rev. Joseph Charles Mitrano|1940-2015
4|12|R.P. François-Régis Frachon|1835-1916
4|13|R.P. Henri Bosc|1837-1901
4|13|Rev. Francis Alphonsus Orsini|1918-1987
4|14|Rev. Dennis Michael Andrews|1941-2010
4|15|R.P. Joseph Fogeron|1867-1944
4|15|Rev. Charles Francis Lyons|1903-1987
4|16|T.R. Nöel Durand|1845-1922
4|16|Rev. Alphonse Anthony de Valk|1932-2020
4|19|Rev. Thomas Anthony Lawlor|1918-1983
4|20|Rev. James Ambrose Raftis|1922-2008
4|21|Mr. Joseph Leo Normandeau|1917-1936
4|21|Rev. John Leonard Dugan|1929-2006
4|22|R.P. Marius Grangeon|1867-1953
4|22|R.P. Fernand Paul André Geneston|1922-1994
4|23|M. Auguste Roure|1836-1860
4|23|R.P. François Régis Hours|1832-1897
4|23|R.P. Gustave Cru|1855-1921
4|23|Rev. Roberto Rojas Peña|1963-2026
4|24|R.P. Justin Reversade|1857-1926
4|24|Rev. Daniel Leo Forestell|1890-1966
4|24|Rev. Edward Gregory Lee|1901-1976
4|25|R.P. Louis Legoux|1855-1935
4|27|R.P. André Fayolle|1791-1867
4|28|Rev. John Clement McIntyre|1905-1945
4|28|Rev. Edward Joseph Hartmann|1904-1957
4|29|Rev. Norbert Joseph Ruth|1909-1998
4|30|Rev. Michael Joseph Mulcahy|1841-1905
4|30|Rev. Michael Joseph Ferguson|1839-1913
5|1|Mr. Gerald Fitzgerald George|1905-1933
5|2|R.P. Hippolyte Peyreplane|1874-1908
5|2|Rev. Albert Pierre DuMouchel|1856-1925
5|2|R.P. Joseph Pierre Cohas|1844-1929
5|3|Rev. John Berchmans Flanagan|1902-1942
5|4|Rev. Edmund Francis Murray|1844-1925
5|4|Rev. Robert McBrady|1848-1936
5|4|Rev. John Arthur Warren|1909-1985
5|4|Rev. Edward Augustine O'Reilly|1924-1997
5|5|R.P. Emile Liogier|1853-1909
5|5|Rev. Patrick Joseph Gallagher|1927-2011
5|6|Rev. Hubert Patrick Coughlin|1902-1983
5|6|Rev. Dennis MacBean Rowan|1927-2017
5|7|R.P. Henri Trouillet|1864-1941
5|7|Rev. Leo Joseph Munnelly|1916-1981
5|7|Rev. Richard Clarence Kinsky|1931-2007
5|8|Rev. Walter Henry Principe|1922-1996
5|9|R.P. Etienne Prévost|1805-1886
5|9|R.P. Jean Crespin|1848-1919
5|9|Rev. Joseph Sylvester Nicholson|1892-1933
5|9|Rev. Joseph Thomas Muckle|1887-1967
5|10|Rev. Svatopluk Stephen Rus|1925-1992
5|12|Rev. Edmund Toussaint Burns|1884-1928
5|12|Rev. John Joseph Miller|1915-1978
5|12|Rev. Patrick Francis McCabe|1928-1992
5|12|Rev. John Baptist Janisse|1925-2003
5|13|R.P. Paul Gourgeon|1834-1899
5|13|R.P. Jean Pierre Grand|1845-1922
5|14|Rev. William Ernest Cummer|1879-1942
5|15|Rev. Lawrence James Lacey|1908-1968
5|15|Rev. Wilfred Samuel Riley|1903-1984
5|16|Rev. Nicholas Earnest Roche|1866-1932
5|18|Rev. John Thomas Kelly|1926-2007
5|18|Rev. Leo Anthony Hetzler|1925-2017
5|19|Rev. Francis De Sales Flood|1911-1977
5|19|Rev. Ralph James MacDonald|1915-1982
5|20|Rev. John Francis Quinn|1925-1996
5|21|Rev. Paul Joseph Meloche|1927-1975
5|21|Rev. Thomas Patrick Warden|1931-2012
5|22|R.P. Jean-Antoine Chanteperdrix|1844-1923
5|22|Rev. John Henry O'Loane|1897-1966
5|22|Rev. Joseph Stanley Murphy|1904-1983
5|23|R.P. Alexis Chanteloube|1837-1878
5|23|Rev. Wilfrid Brian Hennessey|1919-1993
5|24|Rev. Francis John McCarty|1914-1998
5|25|T.R. Charles Léon Roume|1901-1966
5|27|Rev. Thomas Bernard Mailloux|1925-2014
5|28|Rev. Francis Mary I. Walsh|1842-1914
5|28|Rev. Martin Stanley Lynch|1902-1969
5|28|Rev. Arthur Robert Howell|1927-2007
5|28|Rev. John Anthony Poluikis|1923-2012
5|28|Rev. John Jerome Stortz|1925-2013
5|29|Rev. Charles Joseph Armstrong|1906-2001
5|29|Rev. Joseph Lloyd Meyers|1915-2003
5|30|R.P. Basile Hours|1817-1898
5|30|Rev. Patrick Joseph Howard|1866-1929
5|30|R.P. Gabriel Deluche|1879-1955
6|1|Rev. Alexander John Grant|1910-1973
6|1|Rev. Walter Vincent Sullivan|1911-1978
6|1|Rev. Norman Francis Roy|1911-1985
6|1|Rev. Joseph Hunt|1945-2022
6|2|Rev. Charles Collins|1874-1947
6|2|Rev. John Cantius Spratt|1890-1981
6|2|Rev. John Patrick Barry|1919-1986
6|2|Rev. Patrick Joseph Lewis|1910-1988
6|2|Rev. Norbert Clarence Reuss|1912-1999
6|3|R.P. Jacques Vincent Duret|1762-1841
6|3|R.P. Julien Tracol|1796-1885
6|5|R.P. Louis Fayard|1847-1905
6|5|Rev. James Hugh Whelan|1902-1940
6|5|Rev. David Alfred Levack|1922-1960
6|5|Rev. William Joseph Gibbons|1925-1981
6|5|Rev. Charles John Principe|1929-2016
6|5|Rev. John Joseph Hurley|1940-2021
6|6|R.P. Jean Paul Emile Moulin|1830-1873
6|8|R.P. Marcel Pierre Lextrait|1923-2007
6|8|R.P. René Adrien Robert|1925-2015
6|9|Rev. Francis Anthony Walsh|1901-1952
6|10|Rev. John Read Teefy|1848-1911
6|10|Rev. William Ambrose Coyle|1914-1976
6|11|M. Régis Joanny|1869-1894
6|11|Rev. Joseph Gerard Penny|1926-2000
6|12|Rev. Michael Alphonsus Goetz|1920-1980
6|12|Rev. James Francis Blocher|1946-2026
6|13|R.P. Joseph Hilaire|1838-1915
6|13|Rev. Francis James O'Grady|1926-2006
6|13|Rev. James Francis Mallon|1906-2007
6|14|R.P. Marie Denis Gray|1838-1900
6|14|R.P. Irénée-Marie Fouilland|1838-1906
6|14|R.P. Joseph Louis Daniel Maisonneuve|1846-1913
6|14|R.P. Auguste Audibert|1840-1915
6|14|R.P. Louis Alboussière|1873-1926
6|14|Rev. James Edward Daley|1930-1993
6|14|Rev. Gerald Augustin Lalonde|1937-2016
6|15|Rev. Thomas Paul Broadhurst|1932-2018
6|16|Rev. Arthur Jennings O'Leary|1910-1976
6|16|Rev. Robert Alphonse Hall|1931-1989
6|16|Rev. Victor Benjamin Brezik|1913-2009
6|16|Rev. Patrick O'Connor Braden|1924-2016
6|19|R.P. Antoine Louis Germain Deglesne|1798-1856
6|20|Rev. James Wilfrid Dore|1901-1981
6|20|Rev. John Walter Seniawski|1947-1991
6|21|Rev. François-Xavier Semande|1855-1922
6|21|Rev. Frederick Joseph Killaire|1944-1976
6|22|Rev. James Joseph Kelly|1939-1992
6|23|Rev. Edward J. Kennedy|1848-1876
6|23|R.P. Léopold Fayolle|1843-1905
6|23|Rev. Cyril Orville Bergeron|1914-1963
6|23|Rev. Hugh Philip Nolan|1908-1969
6|23|Rev. William Hudson Murray|1888-1976
6|23|Rev. John Patrick Hanrahan|1915-1993
6|23|R.P. Jean Félix Roure|1925-2013
6|23|Rev. David Craig Bittner|1955-2026
6|24|R.P. Jules Raphanel|1857-1887
6|24|Rev. Gerard William Todd|1896-1980
6|24|Rev. Basil Francis Sullivan|1894-1983
6|24|Rev. John Raymond Whitley|1927-2012
6|25|Rev. Robert Joseph Scollard|1908-1993
6|26|R.P. Henri Hourdin|1838-1878
6|28|R.P. Jean Louis Ranc|1821-1902
6|28|Rev. William Joseph Storey|1895-1944
6|28|Rev. Daniel Gordon Macdonald|1927-1996
6|30|Rev. Laurence Brennan|1847-1904
6|30|M. Rev. Denis O'Connor|1841-1911
6|30|Rev. Thomas Aloysius MacDonald|1897-1986
7|3|Rev. James Joseph Guinane|1854-1905
7|4|Rev. Joseph Leo Rivard|1906-1981
7|4|Rev. René Marcel Vermillion|1928-1992
7|4|Rev. Edward Xavier Ronan|1924-2000
7|5|M. Elie Mollaret|1855-1881
7|6|R.P. Jean-Régis Guigon|1868-1928
7|8|Rev. Ronald James Cullen|1915-2010
7|9|Rev. John Peter Ruth|1907-1971
7|10|Rev. Edward Joseph Tighe|1890-1944
7|11|Rev. Francis Sylvester Ruth|1902-1964
7|11|Rev. Richard Bertram Donovan|1923-2007
7|12|Rev. Arthur Joseph Holmes|1930-1994
7|13|Rev. Leo Victor McGrady|1920-1965
7|14|Rev. Eugene Michael Burbott|1921-1962
7|14|Rev. Charles Edward Lynch|1931-2014
7|15|Rev. Gerard Harvey Wilbur|1898-1947
7|16|Rev. John Robert LaCroix|1912-1985
7|16|Rev. Leo Jerome Klem|1913-2012
7|18|Rev. Charles Ralph Koehler|1913-2000
7|19|R.P. Xavier Bouchet|1841-1919
7|19|Rev. Alexander Joseph Denomy|1904-1957
7|20|Rev. Joseph Anthony Trovato|1930-2020
7|20|Rev. Roy Joseph Oggero|1932-2023
7|22|Rev. Robert Joseph Whyte|1921-2014
7|23|Rev. Edmund Eugene Burns|1908-1961
7|23|Rev. Hugh McMahon Foley|1925-2013
7|23|Rev. Raymond Arthur Jackson|1926-2020
7|24|Rev. Michael Vincent Kelly|1863-1942
7|24|Rev. Gerald Frederick McGuigan|1925-1988
7|24|Rev. Jospeh Michael Lanzalaco|1950-2015
7|25|R.P. Henri Gallon|1846-1910
7|25|Rev. Luke Léon Beuglet|1878-1955
7|25|Rev. William Bernard O'Toole|1897-1968
7|25|Rev. Rocco Carmelo Volpe|1930-2007
7|25|Rev. Lawrence James Faye|1915-2008
7|25|Rev. Mark Owen Lee|1930-2019
7|26|Rev. George James Thompson|1904-1969
7|26|Rev. Peter Edward Sheehan|1927-1972
7|26|Rev. John Richard Cavanaugh|1929-2007
7|26|Rev. Frederick John Zakoor|1929-2014
7|27|R.P. Auguste Vielfaure|1855-1908
7|27|R.P. Eugène Jobert|1865-1933
7|28|R.P. Jean François Polly|1803-1854
7|29|T.R. Adrien Fayolle|1837-1898
7|30|Rev. Michael Joseph Pickett|1876-1949
7|31|R.P. Louis Brunel|1841-1913
7|31|R.P. Antoine Victor Perbost|1824-1903
7|31|Rev. James Terence Forestell|1925-2000
8|1|Rev. John M. Morrow|1845-1878
8|1|Rev. John Brian Mullins|1922-1993
8|2|Rev. John Robert Lee|1927-2015
8|4|Rev. John Aloysius Burke|1921-1995
8|5|R.P. Henri Coupat|1805-1868
8|5|R.P. Félix Chabert|1803-1873
8|5|R.P. Hilaire Durand|1836-1880
8|5|Rev. Peter James Beaton|1925-2002
8|5|Rev. Edward Anthony Zemelko|1954-2003
8|6|T.R. Pierre Tourvieille|1780-1859
8|6|Rev. Robert Thomas Chauvin|1926-1989
8|6|Rev. William Charles Marceau|1927-2000
8|7|Rev. Kenneth Bernard McKernan|1937-2019
8|8|Rev. William John Young|1925-2006
8|8|Rev. Gordon William Walker|1928-2022
8|8|Rev. James Neil Carruthers|1935-2025
8|9|T.R. Marie Joseph Octave Descellière|1874-1950
8|9|Rev. John Roger Dougherty|1928-1996
8|10|Rev. Albert Leland Higgins|1908-1991
8|11|Rev. Eugene Carlisle Lebel|1899-1986
8|11|Rev. Ernest Paul Magee|1908-1997
8|11|Rev. George Watha Kosicki|1928-2014
8|12|Rev. Charles Michael Kelly|1900-1963
8|12|Rev. John Henry Menner|1929-2005
8|13|R.P. Jean Antoine Vallon|1775-1840
8|13|Rev. Leroy Neil Kelly|1930-1999
8|16|T.R. Joseph Bovier Lapierre|1757-1838
8|16|R.P. Alphonse Auguste Pagès|1841-1892
8|16|R.P. Auguste Delhomme|1855-1916
8|17|Rev. Bernard Charles (Jess) Owens|1938-2025
8|18|Rev. Arthur Jerome Weiler|1904-1995
8|20|R.P. Auguste Marcoux|1905-1974
8|21|R.P. Jean François Régis Arnoux|1824-1891
8|21|Rev. Leonard Albert McCann|1908-1966
8|21|Rev. James Joseph Bowie|1923-1987
8|22|H.E. George Bernard Flahiff|1905-1989
8|23|Rev. Edward John O'Neill|1856-1935
8|23|Rev. Robert Emmett Lowrey|1897-1974
8|23|Rev. Michael McMahon Sheehan|1925-1992
8|23|Rev. Joseph Anthony Miller|1927-2005
8|23|Rev. William Alden Riegel|1943-2021
8|24|Rev. Frederick Herbert Sohn|1928-2006
8|26|Mr. Edward Gerard McCarroll|1914-1939
8|26|Rev. Michael Bernard Biondi|1924-1980
8|26|Rev. Louis Francis Purcell|1912-1983
8|26|Rev. Francis Joseph Grescoviak|1918-1995
8|27|R.P. Jean-François Pagès|1793-1861
8|27|R.P. Emile Bobichon|1876-1959
8|27|Rev. Louis Joseph Bondy|1894-1985
8|27|Rev. James Francis O'Neill|1915-1997
8|27|Rev. Henry Norbert Clemens|1914-2002
8|27|Rev. Maurice Louis Pierce|1930-2013
8|28|Rev. Edward Blake Coll|1907-1961
8|28|Rev. Donald Bernard Beninger|1914-1971
8|28|Rev. Michael Patrick Cerretto|1940-2014
8|29|Rev. Anthony Vincent Lococo|1907-1987
8|30|R.P. Jean Joseph Marie Aboulin|1841-1931
8|31|Rev. Patrick Joseph Ryan|1840-1917
8|31|Rev. William Oscar Regan|1910-1994
9|1|R.P. Antoine Alphonse Tastevin|1843-1911
9|1|R.P. Benoît Camille Giraud|1872-1946
9|1|Rev. Joseph John Abend|1920-1994
9|2|Rev. Albert Francis Butler|1921-2001
9|3|Rev. Francis Leo Murphy|1903-1974
9|3|Rev. Francis Leo Burns|1905-1979
9|3|Rev. Robert Thomas O'Halloran|1932-2010
9|4|R.P. Denis Prévost|1852-1930
9|4|Rev. Vincent Joseph Fullerton|1910-1967
9|4|Rev. Gerald Vincent Sharpe|1896-1977
9|7|Rev. William Joseph Conway|1924-1961
9|7|Rev. Edwin Charles Garvey|1907-1996
9|9|Rev. John Francis Onorato|1906-1963
9|9|Rev. Eugene Thomas O'Reilly|1927-2003
9|12|R.P. Louis Hilaire|1844-1935
9|12|Rev. Lawrence Charles Finke|1941-2006
9|13|R.P. Victorin Mollier|1838-1888
9|13|Rev. Walter Winthrop Scott|1925-1988
9|13|Rev. James Arnold Megan|1915-1990
9|14|Rev. Felix Clare Malone|1929-1977
9|15|M. Casimir Ladet|1855-1877
9|15|R.P. Edouard Clauzel|1860-1911
9|15|Rev. Terence Patrick McLaughlin|1903-1970
9|15|Rev. John Curtis O'Donoghue|1919-1978
9|15|Rev. Donald Richard Mooney|1925-1992
9|16|Rev. Edward Arthur Bader|1934-2007
9|18|Rev. Grace Joseph Agius|1915-1998
9|18|Rev. John Francis Murphy|1942-2011
9|18|Rev. Jay Francis Walsh|1935-2022
9|20|R.P. Jean Michel Malbos|1839-1907
9|21|Rev. Ernest Lucien Vadnais|1933-1975
9|21|Rev. John Michael Redmond|1934-1981
9|21|Rev. Charles Casper Smith|1933-2002
9|21|Rev. Robert Joseph Matzinger|1929-2022
9|22|Mr. George Barry Cotter|1851-1875
9|22|Rev. Joseph William Pope|1909-1979
9|23|R.P. François-Xavier Clappe|1811-1881
9|23|Rev. Joseph Floyd Crowley|1902-1956
9|23|Rev. Donald Theodore Cooper|1923-1995
9|24|Rev. Thaddeus Trifilo Valencia|1946-2007
9|26|Rev. John Joseph McReavy|1922-1978
9|26|Rev. John Michael Kelly|1911-1986
9|26|Rev. Mario Osbert D'Souza|1956-2017
9|27|Rev. John B. Cushing|1834-1868
9|27|Rev. Michael Thomas Roach|1872-1936
9|27|Rev. Enrique Bravo|1928-2006
9|28|Rev. Joseph Terence Finnigan|1875-1918
9|29|Rev. Anthony John Kelly|1921-2001
9|29|Rev. Robert Edward Power|1936-2007
9|30|Rev. Joseph Gerald McAlpine|1907-1957
9|30|Rev. Joseph George Barnes|1931-1974
9|30|Rev. Francis Christian Boehm|1926-1995
9|30|Rev. Roger Foley DeBilly|1917-1995
10|1|R.P. Marius Philippon|1865-1937
10|1|Rev. Vincent Ignatius Donnelly|1871-1940
10|1|Rev. James Joseph Collins|1919-1992
10|1|Rev. Joseph Blaise Courtney|1922-2003
10|2|Rev. Dennis Joseph Kauffman|1948-2024
10|4|R.P. Louis Michel Monot|1814-1877
10|4|Rev. Robert John Duggan|1935-2015
10|4|Rev. Thomas Joseph McReavy|1925-2020
10|5|R.P. Benjamin Serey|1868-1939
10|5|Rev. John Vincent Corrigan|1907-1962
10|6|Rev. Frederick Daniel Meader|1880-1924
10|6|Rev. John Michael Hussey|1909-1966
10|6|Rev. John Leonard Cullen|1918-2008
10|7|Rev. Vincent Alexander Thomson|1902-1968
10|7|Rev. John Berthold Morrissey|1898-1981
10|9|R.P. Ernest Martin|1856-1935
10|9|Rev. Patrick Alfred Lalonde|1927-1987
10|9|Rev. William Allen Schreiner|1935-1992
10|10|R.P. Louis Bonnaud|1835-1904
10|10|Rev. James Peter Morro|1930-1992
10|10|Rev. James Vincent Thompson|1922-2016
10|11|Rev. Joseph Mullaly Kennedy|1869-1938
10|12|Rev. Matthew Ambrose Killoran|1901-1977
10|12|Rev. William Macbeth Brown|1940-1984
10|12|Rev. John Augustus Fournier|1924-2012
10|14|Rev. Charles Patrick Donovan|1892-1981
10|14|Rev. James Bernard Black|1926-1995
10|15|R.P. Arsène Martin|1862-1909
10|15|Rev. George Ernest Silvester|1920-2001
10|16|Mr. Joseph Edward Tallon|1897-1925
10|16|Rev. Leslie Joseph Vasek|1923-1967
10|17|T.R. Jean Mathieu Soulerin|1807-1879
10|17|Rev. Gerald Falconbridge Anglin|1902-1996
10|18|Rev. John Leonard Boscoe|1943-2023
10|19|Rev. John Francis Collins|1908-1969
10|20|R.P. Marius Gourgeon|1849-1936
10|20|Rev. Arthur Rynold Looby|1915-1993
10|20|Rev. Arthur Thomas Hathaway|1926-2022
10|21|T.R. Victorin Marijon|1851-1931
10|22|R.P. Antoine Bord|1826-1902
10|23|Rev. Laurence Kennedy Shook|1909-1993
10|23|Rev. John Gerald French|1908-1996
10|24|Rev. James Joseph Keon|1925-2012
10|25|M. Isidore Chalaye|1829-1849
10|25|Mr. Thomas James McGwan|1890-1918
10|25|Rev. John James Gaughan|1928-2000
10|25|Rev. Christopher Andrew Iredale|1931-2015
10|26|R.P. Auguste de Montgolfier|1800-1859
10|26|R.P. Jean Henri Guigal|1842-1921
10|26|Rev. Wilfrid Joseph Murphy|1903-1970
10|27|R.P. Joannès Etienne Oudin|1851-1913
10|27|Rev. Joseph Patrick Dillon|1897-1953
10|27|Rev. Robert Michael Montague|1918-1968
10|27|Rev. Thomas Michael Miller|1917-2012
10|27|Rev. Cecil Harold Zinger|1933-2017
10|28|T.R. Joseph Marie Julien Actorie|1803-1864
10|28|Rev. Edward Leo Pokriefka|1896-1954
10|28|Rev. Hugh Vincent Mallon|1910-1978
10|28|Rev. James Gareth Poupore|1922-2012
10|28|Sr. Javier de la Rosa Luna|1970-2018
10|29|R.P. Pierre Joseph Chalandard|1841-1915
10|31|Rev. James J. O. McEvoy|1842-1911
11|1|R.P. Charles Vincent|1828-1890
11|1|Rev. John Michael Wesley|1940-1980
11|1|Rev. Howard Joseph Keon|1920-2016
11|2|R.P. Emile Aureille|1870-1937
11|3|Rev. Edmund Michael Young|1907-1997
11|3|Rev. Leo Jerome Klosterman|1927-2007
11|3|Rev. Angelo Dominic Bovenzi|1939-2008
11|4|Rev. Paul Thomas Sheehan|1931-1996
11|4|Rev. Brian Philip Higgins|1926-2016
11|4|Rev. Richard Joseph Elmer|1928-2021
11|5|Rev. Ronald Alexander Borg|1943-2023
11|6|Rev. Arthur Joseph Morley|1876-1914
11|6|R.P. Jean-Marie Frachon|1871-1945
11|6|Rev. John Douglas Hilmer|1961-2025
11|6|Rev. Joseph Thomas Walsh|1936-2025
11|7|Mr. Joseph Anthony Lodato|1893-1918
11|7|Rev. Charles William Delaney|1942-1986
11|9|Rev. David William Bauer|1924-1988
11|10|Rev. William Alexander Sheehan|1904-1967
11|11|V.R. Robert Francis Forster|1873-1929
11|11|Rev. Lionel Richard Sands|1932-2006
11|12|Rev. John Joseph Ward|1941-2009
11|13|Rev. Marvin Walter LaBute|1937-2018
11|14|R.P. Jean Balandreau|1840-1910
11|14|Rev. John Joseph Purcell|1878-1918
11|14|R.P. Jean-Marie Tarabout|1840-1930
11|14|Rev. Richard John Schiefen|1932-2003
11|15|Rev. James Austin Whelihan|1902-1986
11|15|Rev. Peter Julian Michael Swan|1919-2018
11|15|Rev. Martin Janez Dimnik|1941-2020
11|16|R.P. François Jean Vaschalde|1828-1892
11|16|Rev. William Patrick McGee|1888-1975
11|18|Rev. William Francis Nigh|1902-1974
11|18|Rev. Donald Earl Benwitz|1940-1983
11|18|Rev. Edward Cecil Pappert|1914-1990
11|18|Rev. Anthony John Ruth|1914-1998
11|18|Rev. Philip Wallace Platt|1925-2021
11|19|R.P. Vincent Augustin Bufferne|1849-1913
11|19|Rev. Wilfred Steidel Canning|1927-2025
11|20|Rev. Edward John Sullivan|1918-1967
11|21|Rev. Michael John Ryan|1869-1947
11|21|Rev. Richard Shakeshaft Wood|1916-1956
11|22|R.P. Frédéric Martaresche|1834-1905
11|22|R.P. Louis Descellière|1841-1922
11|22|Rev. Richard Thomas Burke|1859-1941
11|22|Rev. Anthony Edward Doser|1925-2009
11|23|Rev. James Joseph Murphy|1888-1967
11|24|Rev. Joseph Thomas Culliton|1935-1990
11|24|Rev. Donald Joseph McNeil|1916-2001
11|25|Rev. Thomas Rodric McGillis|1912-1987
11|25|Rev. Peter Joseph Etlinger|1918-2012
11|25|Rev. William John Sheehan|1937-2018
11|26|R.P. Jean-Louis Chambon|1841-1900
11|26|R.P. Jean-Claude-Marie Mazenod|1849-1922
11|26|R.P. Jules Deschanel|1844-1933
11|26|Rev. Bernard Michael Regan|1909-1979
11|27|R.P. Ferdinand Ozil|1851-1901
11|27|Rev. Louis Charles Edward McLean|1925-1990
11|27|Rev. Edwin Joseph Kline|1927-2018
11|28|V.R. Henry Carr|1880-1963
11|29|Rev. Joseph Patrick Sharpe|1875-1918
11|30|Rev. Ronald Stanly Pazik|1926-2009
11|30|V.R. Kenneth John Decker|1938-2015
12|1|Rev. William John Duggan|1910-1973
12|1|Rev. Phillip Anthony Acquaro|1940-2022
12|2|R.P. Marcel Mourgue|1859-1935
12|2|Rev. Joseph Edward McGahey|1902-1945
12|2|Rev. Thomas Delbert Amlin|1935-1979
12|2|Rev. Francis Michael Sheahan|1912-1997
12|2|Rev. John Cuthbert Wick|1928-2005
12|2|Rev. Edward Arthur Roberts|1927-2015
12|3|Rev. Herbert Basil Regan|1912-1958
12|3|Rev. Charles Joseph Lavery|1915-1985
12|3|Rev. John Clifford Murray|1930-2012
12|3|Rev. Gerald Francis Dillon|1932-2020
12|4|Rev. Michael Patrick Christian|1859-1934
12|4|Rev. Thomas James Vahey|1898-1955
12|5|R.P. Gustave Coulet|1850-1930
12|6|R.P. Maurice Boucher|1855-1942
12|6|Rev. John Joseph Sullivan|1892-1967
12|6|Rev. John Joseph Francis Firth|1914-1999
12|6|R.P. Félix Jules Pouzol|1911-2000
12|6|Rev. William Henry Irwin|1932-2023
12|7|Rev. Neil Jerome McNulty|1862-1934
12|7|R.P. Alphonse Marcelin Colomb|1872-1943
12|7|Rev. Daniel Leo Dillon|1889-1948
12|7|Rev. Joseph Jeremiah Timmons|1912-1956
12|7|Rev. Robert Matthew Fischette|1913-1970
12|8|R.P. Pierre Bonnet|1830-1870
12|8|R.P. Camille Joseph Meyzonnier|1843-1891
12|8|Rev. William Hilary O'Brien|1922-1998
12|8|Rev. Thomas Francis Mohan|1929-2013
12|9|R.P. Louis-Marie Mazet|1873-1967
12|9|Rev. John Patrick Wheeler|1939-2025
12|10|Rev. John Wilbert Meyer|1906-1969
12|10|V.R. Joseph Charles Wey|1910-2000
12|10|Rev. Hugh John Curran|1925-2010
12|11|Rev. Thomas Gregory Mallon|1908-1991
12|11|Rev. Edward William Donoher|1923-2010
12|12|R.P. Jean Jacques Giraud|1817-1891
12|13|Rev. William Edward Coughlin|1930-2005
12|13|Rev. Gordon James Judd|1941-2019
12|14|Rev. Patrick Joseph Gorman|1917-2003
12|15|Mr. James Leo Ruth|1911-1934
12|15|Rev. Edward Francis Flanagan|1913-1977
12|16|R.P. Charles-Joseph Faure|1818-1887
12|18|R.P. Pierre Menut|1839-1895
12|18|Rev. Daniel Cushing|1850-1928
12|18|R.P. Emile Suchet|1854-1929
12|18|Rev. Joseph Leo O'Donnell|1900-1984
12|18|Rev. Raymond Louis Prince|1906-1992
12|18|R.P. Michel Etienne Deglène|1936-2016
12|19|R.P. Frédéric Bernard Fournier|1839-1895
12|19|Rev. Richard Patrick Drohan|1873-1908
12|19|Rev. James Alexander Donlon|1904-1987
12|19|Rev. John Forrest Robbins|1943-2009
12|20|Rev. Emil J. Plourde|1879-1934
12|20|Rev. John Redmond McLaughlin|1916-1954
12|20|Rev. James William Embser|1905-1984
12|20|Rev. James O'Reilly Enright|1924-2010
12|20|Rev. James Kelsey McConica|1930-2023
12|21|Rev. Robert Grace Miller|1912-1997
12|21|Rev. Daniel Wai-Sang Chui|1959-2014
12|22|Rev. John Wilfrid Dwyer|1898-1991
12|23|Rev. Léon Edouard Cherrier|1834-1924
12|23|Rev. Ernest Joseph Lajeunesse|1901-1991
12|23|Rev. Albert Robert Gaelens|1932-2023
12|24|R.P. Adrien Chomel|1848-1906
12|24|Rev. Edward Leonard Rush|1894-1979
12|25|Rev. Donald Charles Furlong|1924-2014
12|25|Rev. George Henry Hosko|1939-2016
12|28|R.P. Louis Romans|1850-1922
12|28|Rev. Henry Stanislaus Bellisle|1891-1938
12|28|Rev. Joseph Barrett Dorsey|1915-1988
12|29|Rev. Peter Kingdon Mosteller|1928-2011
12|31|Mr. Michael Francis Gorman|1846-1878
12|31|Rev. John Joseph Glavin|1895-1978
12|31|Rev. Francis Joseph Launtrie|1927-2005
    """.trimIndent()

    fun monthNameEs(month: Int): String = when (month) {
        1 -> "enero"
        2 -> "febrero"
        3 -> "marzo"
        4 -> "abril"
        5 -> "mayo"
        6 -> "junio"
        7 -> "julio"
        8 -> "agosto"
        9 -> "septiembre"
        10 -> "octubre"
        11 -> "noviembre"
        12 -> "diciembre"
        else -> ""
    }

    fun monthNameDisplay(month: Int, lang: AppLanguage = AppLanguage.ES): String = when (lang) {
        AppLanguage.EN -> when (month) {
            1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
            5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
            9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
            else -> ""
        }
        AppLanguage.FR -> when (month) {
            1 -> "Janvier"; 2 -> "Février"; 3 -> "Mars"; 4 -> "Avril"
            5 -> "Mai"; 6 -> "Juin"; 7 -> "Juillet"; 8 -> "Août"
            9 -> "Septembre"; 10 -> "Octobre"; 11 -> "Novembre"; 12 -> "Décembre"
            else -> ""
        }
        AppLanguage.ES -> when (month) {
            1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"
            5 -> "Mayo"; 6 -> "Junio"; 7 -> "Julio"; 8 -> "Agosto"
            9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
            else -> ""
        }
    }

    /**
     * Construye la lista completa de todos los hermanos difuntos combinando las semblanzas enriquecidas
     * con las más de 530 entradas oficiales de la Necrología Basiliana (Noviembre 2026).
     */
    fun buildCompleteDeceasedBrothersList(featuredList: List<DeceasedBrother>): List<DeceasedBrother> {
        val parsedEntries = rawEntriesText.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() && it.contains("|") }
            .mapNotNull { line ->
                val parts = line.split("|")
                if (parts.size < 4) return@mapNotNull null
                val month = parts[0].trim().toIntOrNull() ?: return@mapNotNull null
                val day = parts[1].trim().toIntOrNull() ?: return@mapNotNull null
                val rawName = parts[2].trim()
                val yearsPart = parts[3].trim()
                val yParts = yearsPart.split("-")
                val bYear = yParts.getOrNull(0)?.trim()?.toIntOrNull() ?: 0
                val dYear = yParts.getOrNull(1)?.trim()?.toIntOrNull() ?: 0
                RawNecrologyEntry(
                    month = month,
                    day = day,
                    rawName = rawName,
                    birthYear = bYear,
                    deathYear = dYear
                )
            }

        val result = mutableListOf<DeceasedBrother>()
        val usedFeaturedIds = mutableSetOf<String>()

        for ((index, entry) in parsedEntries.withIndex()) {
            val cleanRawLower = entry.rawName.lowercase()
                .replace("rev.", "")
                .replace("r.p.", "")
                .replace("t.r.", "")
                .replace("v.r.", "")
                .replace("m. rev.", "")
                .replace("h.e.", "")
                .replace("mr.", "")
                .replace("sr.", "")
                .replace("m.", "")
                .trim()
            val rawTokens = cleanRawLower.split(" ", "-", "(", ")").filter { it.length >= 4 }
            val lastSurname = rawTokens.lastOrNull() ?: cleanRawLower

            // Buscar si este hermano ya tiene una ficha biográfica destacada
            val matchedFeatured = featuredList.firstOrNull { feat ->
                val fLower = feat.name.lowercase()
                val sameDeathYear = feat.birthAndDeathYears.contains(entry.deathYear.toString()) ||
                    feat.birthAndDeathYears.contains(entry.birthYear.toString())
                sameDeathYear && fLower.contains(lastSurname) && rawTokens.count { fLower.contains(it) } >= 1
            }

            val mName = monthNameEs(entry.month)
            val fullDatePassed = "${entry.day} de $mName de ${entry.deathYear}"
            val computedAge = if (entry.birthYear > 1700 && entry.deathYear >= entry.birthYear) {
                entry.deathYear - entry.birthYear
            } else null
            val formattedYears = "${entry.birthYear} - ${entry.deathYear}"

            if (matchedFeatured != null && usedFeaturedIds.add(matchedFeatured.id)) {
                result.add(
                    matchedFeatured.copy(
                        age = matchedFeatured.age ?: computedAge,
                        birthAndDeathYears = formattedYears,
                        datePassed = fullDatePassed,
                        monthPassed = entry.month,
                        dayPassed = entry.day
                    )
                )
            } else {
                val specialInfo = resolveSpecialNecrologyBio(entry, fullDatePassed)
                val displayName = if (entry.rawName.endsWith("CSB", ignoreCase = true)) {
                    entry.rawName
                } else {
                    "${entry.rawName}, CSB"
                }
                result.add(
                    DeceasedBrother(
                        id = "necro_${entry.month}_${entry.day}_${index}",
                        name = displayName,
                        age = computedAge,
                        birthAndDeathYears = formattedYears,
                        datePassed = fullDatePassed,
                        category = specialInfo.first,
                        titleOrRole = specialInfo.second,
                        memorialQuote = specialInfo.third,
                        monthPassed = entry.month,
                        dayPassed = entry.day
                    )
                )
            }
        }

        // Agregar cualquier hermano destacado que no estuviera aún en la lista
        for (feat in featuredList) {
            if (feat.id !in usedFeaturedIds) {
                result.add(feat)
            }
        }

        return result
    }

    private fun resolveSpecialNecrologyBio(
        entry: RawNecrologyEntry,
        fullDatePassed: String
    ): Triple<DeceasedCategory, String, String> {
        val n = entry.rawName.lowercase()

        // Casos especiales recientes 2024-2026 y hermanos de América Latina / Colombia / México
        return when {
            n.contains("roberto rojas") -> Triple(
                DeceasedCategory.COLOMBIA_MEMORIA,
                "Sacerdote Basiliano Colombiano • Pascua 2026",
                "Amado hermano y sacerdote basiliano colombiano (1963-2026), llamado a la Casa del Padre el 23 de abril de 2026. Recordado con inmensa gratitud por su entrega pastoral, alegría fraterna y fidelidad al carisma de San Basilio en Colombia."
            )
            n.contains("james francis blocher") || n.contains("blocher") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote Basiliano • Pascua Junio 2026",
                "Fiel sacerdote de la Congregación de San Basilio (1946-2026), quien partió a la Pascua eterna el 12 de junio de 2026 tras una vida consagrada a la educación, la oración y la fraternidad comunitaria."
            )
            n.contains("david craig bittner") || n.contains("bittner") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote Basiliano • Pascua Junio 2026",
                "Querido sacerdote basiliano (1955-2026), llamado a la presencia del Señor el 23 de junio de 2026. Su testimonio de bondad, servicio pastoral y cercanía permanece vivo en la comunidad."
            )
            n.contains("james joseph gaunt") || n.contains("gaunt") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote Basiliano • Pascua Febrero 2025",
                "Sacerdote y educador basiliano (1934-2025), conmemorado el 8 de febrero. Dedicó su vida con sabiduría y paciencia al servicio del Evangelio."
            )
            n.contains("donald francis finlay") || n.contains("finlay") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote y Bibliotecario Basiliano • Pascua Abril 2025",
                "Sacerdote basiliano (1936-2025), conmemorado el 4 de abril. Custodio del saber humanista y teológico en el Pontifical Institute of Mediaeval Studies y St. Michael's College."
            )
            n.contains("dennis joseph kauffman") || n.contains("kauffman") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote Basiliano • Pascua Octubre 2024",
                "Sacerdote basiliano (1948-2024), conmemorado el 2 de octubre. Pastor cercano y servidor fiel en las misiones y parroquias de la Congregación."
            )
            n.contains("joseph ian boyd") || n.contains("ian boyd") -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote y Erudito Chestertoniano • Pascua Enero 2024",
                "Sacerdote basiliano (1935-2024), conmemorado el 10 de enero. Reconocido mundialmente como fundador del Chesterton Review y maestro de literatura y fe."
            )
            n.contains("javier de la rosa luna") -> Triple(
                DeceasedCategory.COLOMBIA_MEMORIA,
                "Hermano / Escolástico Basiliano en América Latina",
                "Recordado con profundo cariño fraterno en la Necrología Basiliana el 28 de octubre (1970-2018) por su testimonio de fe, sencillez y entrega comunitaria."
            )
            n.contains("josé isidro castillo") || n.contains("isidro castillo") -> Triple(
                DeceasedCategory.COLOMBIA_MEMORIA,
                "Sacerdote Basiliano Hispanoamericano",
                "Conmemorado el 28 de enero (1957-1996). Entregó su juventud sacerdotal con ardor misionero y amor entrañable al pueblo de Dios."
            )
            n.contains("raymond páramo") || n.contains("paramo") ||
                n.contains("sepúlveda") || n.contains("sepulveda") ||
                n.contains("enrique bravo") || n.contains("trifilo valencia") -> Triple(
                DeceasedCategory.COLOMBIA_MEMORIA,
                "Sacerdote Basiliano • Misión y Pastoral Hispana",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Recordado con gratitud el $fullDatePassed por su ministerio generoso en la familia de San Basilio."
            )
            entry.deathYear >= 2022 -> Triple(
                DeceasedCategory.RECENTES_2025_2026,
                "Sacerdote Basiliano • Pascua Reciente (${entry.deathYear})",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Conmemorado en la Necrología Oficial el $fullDatePassed por su vida consagrada a la bondad, la disciplina y la ciencia."
            )
            entry.rawName.startsWith("T.R.") || entry.rawName.startsWith("V.R.") ||
                entry.rawName.startsWith("M. Rev.") || entry.rawName.startsWith("H.E.") -> Triple(
                DeceasedCategory.SUPERIORES_GENERALES,
                "Superior / Prelado Basiliano Ilustre (Necrología Oficial)",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Conmemorado el $fullDatePassed en la Necrología Oficial de la Congregación de San Basilio."
            )
            entry.rawName.startsWith("R.P.") -> Triple(
                DeceasedCategory.NECROLOGIA_GENERAL,
                "Sacerdote Basiliano (Tradición Francesa • Necrología Oficial)",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Sacerdote basiliano conmemorado el $fullDatePassed por su fidelidad al carisma de Annonay."
            )
            entry.rawName.startsWith("M.") || entry.rawName.startsWith("Mr.") || entry.rawName.startsWith("Sr.") -> Triple(
                DeceasedCategory.NECROLOGIA_GENERAL,
                "Escolástico / Hermano Basiliano en Formación (Necrología Oficial)",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Hermano en formación llamado prematuramente a la Casa del Padre el $fullDatePassed."
            )
            else -> Triple(
                DeceasedCategory.NECROLOGIA_GENERAL,
                "Sacerdote Basiliano (Necrología Oficial C.S.B.)",
                "«Laudemus viros gloriosos — Hagamos el elogio de los hombres ilustres.» Conmemorado en la Necrología Basiliana el $fullDatePassed por su entrega al Evangelio en bondad, disciplina y ciencia."
            )
        }
    }
}
