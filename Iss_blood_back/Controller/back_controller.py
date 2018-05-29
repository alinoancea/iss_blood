from Repository.repository_manager import RepoManager
from Service.service_common import ServiceCommon
from Service.service_donator import ServiceDonator
from Service.service_medic import ServiceMedic
from Service.service_sange import ServiceSange

from Service.service_staff_transfuzie import ServiceStaffTransfuzie
from Utils.orm import ORM


class BackController:
    def __init__(self, db_config):
        self.repo_manager = None
        orm = ORM(db_config)
        self.repo_manager = RepoManager(orm)
        self.service_common = ServiceCommon(self.repo_manager, orm)
        self.service_donator = ServiceDonator(self.repo_manager, orm)
        self.service_medic = ServiceMedic(self.repo_manager, orm)
        self.service_transfuzie = ServiceStaffTransfuzie(self.repo_manager, orm)
        self.service_sange = ServiceSange(self.repo_manager, orm)
        self.service_administrator = None

    def login(self, user, password):
        return self.service_common.login(user, password)

    def register(self, register_info):
        return self.service_common.register(register_info)

    def user_trimite_formular(self, formular, username):
        return self.service_donator.insert_formular_user(formular, username)

    def staff_trimite_formular(self, formular):
        return self.service_transfuzie.insert_formular_staff(formular)

    def staff_cerere_formulare_donari(self,id_locatie):
        return self.service_transfuzie.get_cereri(id_locatie)

    def staff_update_formular_donare(self, formular_donare, id_locatie, analiza=None):
        self.manage_request(formular_donare, id_locatie, analiza)
        return self.service_transfuzie.update_formular(formular_donare)

    def create_sange_brut(self, id_donator, id_locatie):
        self.service_sange.create_sange_brut(id_donator, id_locatie)

    def create_sange_prelucrat(self,id_donator):
        self.service_sange.create_sange_prelucrat(id_donator)

    def create_analiza(self, id_donator, alt, sif, antihtlv, antihtcv, antihiv, hb):
        self.service_sange.create_analiza(id_donator, alt, sif, antihtlv, antihtcv, antihiv, hb)

    def get_stoc_curent(self,id_locatie):
        return self.service_sange.get_stoc_curent(id_locatie)

    def manage_request(self, formular_donare, id_locatie, analiza=None):
        status = formular_donare.status
        id_donator = self.get_id_donator(formular_donare)

        if status.upper() == 'PRELEVARE':
            self.create_sange_brut(id_donator, id_locatie)
        elif status.upper() == 'PREGATIRE':
            self.create_sange_prelucrat(id_donator)
        elif status.upper() == 'NONCONFORM' and analiza is not None:
            alt = analiza.alt
            sif = analiza.sif
            antihtlv = analiza.antihtlv
            antihtcv = analiza.antihtcv
            antihiv = analiza.antihiv
            hb = analiza.hb
            self.create_analiza(id_donator, alt, sif, antihtlv, antihtcv, antihiv, hb)

    def get_id_donator(self, formular_donare):
        return self.service_transfuzie.get_id_donator(formular_donare)
