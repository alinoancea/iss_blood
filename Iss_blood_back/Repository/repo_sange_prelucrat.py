from Repository.i_repository import IRepository


class RepositorySangePrelucrat(IRepository):
    def __init__(self, db):
        super().__init__(db)

    def get_id_judet(self,id_locatie):
        locatie = self.db.select('Locatie', ['id'], [id_locatie], first=True)
        return self.db.select('Localitate', ['id'], [locatie.id_localitate], first=True).id_judet

    def insert(self, sange_prelucrat):
        table_name = 'SangePrelucrat'
        specific_col_names = ['id_sange_brut', 'tip', 'id_locatie', 'status']
        specific_vals = [sange_prelucrat.id_sange_brut,
                         sange_prelucrat.tip,
                         sange_prelucrat.id_locatie,
                         sange_prelucrat.status]

        try:
            self.db.insert(table_name, specific_col_names, specific_vals)
        except...:
            return 2, "Database error"

        return 0, "Added successfully"

    def delete(self,id_sange_brut):

        table_name='SangePrelucrat'
        try:
            self.db.delete(table_name, columns=['id_sange_brut'], values=[id_sange_brut])
        except...:
            return 2,"Database error"

        return 0, "Deleted successfully"

    def get_id_locatie_noua_from_cerere(self,cerere):
        medic = self.db.select('Medic', ['id_user'], [cerere.id_medic], first=True)
        return medic.id_locatie

    def functie(self, cerere, id_locatie_curenta, grupa, rh, plasma, tromobocite, globule_rosii):
        pacient = self.db.select('Pacient', ['id'], [cerere.id_pacient], first=True)

        columns = ['beneficiar_CNP', 'beneficiar_full_name', 'grupa', 'rh', 'status']
        values = [pacient.cnp, pacient.nume, grupa, rh, 'Distribuire']
        formulare_donari = self.db.select('FormularDonare', columns, values)

        id_locatie_noua = self.get_id_locatie_noua_from_cerere(cerere)
        id_judet = self.get_id_judet(id_locatie_curenta)

        for formular in formulare_donari:
            sange_brut = self.db.select('SangeBrut',
                                        columns=['status', 'grupa', 'rh', 'id_donator'],
                                        values=['Impartita', grupa, rh, formular.id_donator],
                                        first=True)
            if id_judet == self.get_id_judet(sange_brut.id_locatie_recoltare):

                lista_pungi = self.db.select('SangePrelucrat',
                                             columns=['id_sange_brut', 'status'],
                                            values=[sange_brut.id, 'Depozitat'])
                ok = False
                for punga in lista_pungi:
                        if punga.tip == 'Plasma' and plasma != 0:
                            plasma -= 1
                            self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)
                        if punga.tip == 'Trombocite' and tromobocite != 0:
                            tromobocite -= 1
                            self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)
                        if punga.tip == 'Globule_rosii' and globule_rosii != 0:
                            globule_rosii -= 1
                            self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)
                        ok = True

                if ok:
                    self.logger.debug("PULAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
                    pacient.donatori_preferentiali = pacient.donatori_preferentiali + 1

        return plasma, tromobocite, globule_rosii

    def send_pungi(self, id_locatie_curenta, id_cerere, grupa, rh, plasma, tromobocite, globule_rosii):

        cerere = self.db.select('CereriSange', ['id'], [id_cerere], first=True)

        plasma, tromobocite, globule_rosii = self.functie(cerere, id_locatie_curenta, grupa, rh, plasma, tromobocite,
                                                          globule_rosii)

        if plasma > 0 or tromobocite > 0 or globule_rosii > 0:

            id_locatie_noua = self.get_id_locatie_noua_from_cerere(cerere)

            id_judet = self.get_id_judet(id_locatie_curenta)
            self.logger.debug(id_judet)

            sange_brut_locatie = self.db.select('SangeBrut',
                                                columns=['status', 'grupa', 'rh'],
                                                values=['Impartita', grupa, rh])
            for sange_brut in sange_brut_locatie:
                if id_judet == self.get_id_judet(sange_brut.id_locatie_recoltare):
                    lista_pungi = self.db.select('SangePrelucrat',
                                                 columns=['id_sange_brut', 'status'],
                                                 values=[sange_brut.id, 'Depozitat'])
                    for punga in lista_pungi:
                        id_judet_punga = self.get_id_judet(punga.id_locatie)
                        self.logger.debug(id_judet_punga)
                        if id_judet == id_judet_punga:
                            if punga.tip == 'Plasma' and plasma != 0:
                                plasma -= 1
                                self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)
                            if punga.tip == 'Trombocite' and tromobocite != 0:
                                tromobocite -= 1
                                self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)
                            if punga.tip == 'Globule_rosii' and globule_rosii != 0:
                                globule_rosii -= 1
                                self.update_status_by_id_punga(punga.id, 'Folosit', id_locatie_noua)


    def get_stoc_curent_by_grupa_rh(self, id_locatie, grupa_ceruta, rh_cerut):

        # Index : 0 -  Plasma, 1 - Tromobocite , 2 - Globule_rosii
        rezultat = [0, 0, 0]

        # remove on production
        id_judet = self.get_id_judet(id_locatie)
        sange_brut_locatie = self.db.select('SangeBrut',
                                            columns=['status', 'grupa', 'rh'],
                                            values=['Impartita', grupa_ceruta, rh_cerut])
        self.logger.debug("Got Sange brut: {}".format(sange_brut_locatie))
        for sange_brut in sange_brut_locatie:
            if id_judet == self.get_id_judet(sange_brut.id_locatie_recoltare):
                lista_pungi = self.db.select('SangePrelucrat',
                                             columns=['id_sange_brut', 'status'],
                                             values=[sange_brut.id, 'Depozitat'])
                for punga in lista_pungi:
                    # remove if on production
                    id_judet_punga = self.get_id_judet(punga.id_locatie)
                    self.logger.debug(id_judet_punga)
                    if id_judet == id_judet_punga:
                        if punga.tip == 'Plasma':
                            rezultat[0] += 1
                        elif punga.tip == 'Trombocite':
                            rezultat[1] += 1
                        else:
                            rezultat[2] += 1

        dictionar = {"Plasma": rezultat[0],
                     "Trombocite": rezultat[1],
                     "Globule_rosii": rezultat[2]}
        self.logger.debug("Return stoc curent pentru locatie {} grupa {} rh {}: {}".format(id_locatie, grupa_ceruta,
                                                                                           rh_cerut, dictionar))
        return dictionar

    def get_stoc_curent(self, id_locatie):
        sange_prelucrat = self.db.select('SangePrelucrat',
                                        columns=["id_locatie", "status"],
                                        values=[id_locatie, "Depozitat"])
        globule_rosii= len([sange for sange in sange_prelucrat if sange.tip == 'Globule_rosii'])
        trombocite = len([sange for sange in sange_prelucrat if sange.tip == 'Trombocite'])
        plasma = len([sange for sange in sange_prelucrat if sange.tip == 'Plasma'])

        self.logger.debug("Returning stock for location {} globule_rosii: {}, trombocite: {}, plasma: {}.".format(
            id_locatie, globule_rosii, trombocite, plasma))
        return globule_rosii, trombocite, plasma

    def update_status_by_id_sange_brut(self, id_sange_brut, status):

        table_name = 'SangePrelucrat'
        columns_where = ['id_sange_brut']
        values_where = [id_sange_brut]

        specific_col_names = ['status']
        specific_vals = [status]
        try:
            self.db.update(table_name, columns_where = columns_where,
                           values_where=values_where, columns=specific_col_names,
                           values=specific_vals)
        except...:
            return 2, "Database error"

        return 0, "Updated successfully"

    def update_status_by_id_punga(self, id_punga, status, id_locatie_noua):

        table_name = 'SangePrelucrat'
        columns_where = ['id']
        values_where = [id_punga]

        specific_col_names = ['status', 'id_locatie']
        specific_vals = [status, id_locatie_noua]
        try:
            self.db.update(table_name, columns_where=columns_where,
                           values_where=values_where, columns=specific_col_names,
                           values=specific_vals)
        except...:
            return 2, "Database error"

        return 0, "Updated successfully"
