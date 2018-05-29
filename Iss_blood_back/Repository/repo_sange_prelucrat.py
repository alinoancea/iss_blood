from Repository.i_repository import IRepository


class RepositorySangePrelucrat(IRepository):
    def __init__(self, db):
        super().__init__(db)

    def add(self, sange_prelucrat):
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
            self.db.delete('CereriSange', columns=['id_sange_brut'], values=[id_sange_brut])
        except...:
            return 2,"Database error"

        return 0, "Deleted successfully"

    def get_stoc_curent_by_grupa_rh(self, id_locatie, grupa_ceruta, rh_cerut):

        # Index : 0 -  Plasma, 1 - Tromobocite , 2 - Globule_rosii
        rezultat = [0,0,0]

        sange_brut_locatie = self.db.select('SangeBrut',
                                            columns=['id_locatie_recoltare', 'grupa', 'rh'],
                                            values=[id_locatie, grupa_ceruta, rh_cerut])
        for sange_brut in sange_brut_locatie:
            lista_pungi = self.db.select('SangePrelucrat',
                                         columns=['id_sange_brut', 'status'],
                                         values=[sange_brut.id, 'Depozitat']
                                         )
            for punga in lista_pungi:
                if punga.tip == 'Plasma':
                    rezultat[0] += 1
                elif punga.tip == 'Trombocite':
                    rezultat[1] += 1
                else:
                    rezultat[2] += 1

        return rezultat

    def update_status_by_id_sange_brut(self,id_sange_brut, status):

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
