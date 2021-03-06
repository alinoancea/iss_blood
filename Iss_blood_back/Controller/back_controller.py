from Repository.repository_manager import RepoManager

from Service.service_common import ServiceCommon
from Service.service_donator import ServiceDonator
from Service.service_medic import ServiceMedic
from Service.service_sange import ServiceSange
from Service.service_staff_transfuzie import ServiceStaffTransfuzie
from Service.service_chat import ServiceChat

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
        self.service_chat = ServiceChat(self.repo_manager, orm)
        self.service_administrator = None

    def login(self, user, password):
        return self.service_common.login(user, password)

    def register(self, register_info):
        return self.service_common.register(register_info)

    def add_pacient(self, id_medic, nume_pacient, cnp_pacient, grupa_sange_pacient, rh_pacient):
        return self.service_medic.add_pacient(id_medic, nume_pacient, cnp_pacient, grupa_sange_pacient, rh_pacient)

    def user_trimite_formular(self, formular, username):
        return self.service_donator.insert_formular_user(formular, username)

    def staff_trimite_formular(self, formular):
        return self.service_transfuzie.insert_formular_staff(formular)

    def staff_cerere_formulare_donari(self,id_locatie):
        return self.service_transfuzie.get_cereri(id_locatie)

    def staff_update_formular_donare(self, formular_donare, id_locatie, analiza=None, staff_full_name=None):
        self.manage_request(formular_donare, id_locatie, analiza=analiza, staff_full_name=staff_full_name)
        return self.service_transfuzie.update_formular(formular_donare)

    def create_sange_brut(self, id_donator, id_locatie, staff_full_name):
        self.service_sange.create_sange_brut(id_donator, id_locatie, staff_full_name)

    def create_sange_prelucrat(self,id_donator):
        self.service_sange.create_sange_prelucrat(id_donator)

    def create_analiza(self, id_donator,grupa, rh, alt, sif, antihtlv, antihtcv, antihiv, hb, id_formular):
        self.service_sange.create_analiza(id_donator, grupa, rh, alt, sif, antihtlv, antihtcv, antihiv, hb, id_formular)

    def get_stoc_curent(self,id_locatie):
        return self.service_sange.get_stoc_curent(id_locatie)

    def send_pungi(self, id_locatie_curenta, id_cerere, grupa, rh, plasma, tromobocite, globule_rosii):

        stoc_curent = self.get_stoc_curent(id_locatie_curenta)

        stoc_specific = stoc_curent[str(grupa).upper()+'_'+str(rh).lower()]

        numar_plasma = stoc_specific["Plasma"]
        numar_trombocite = stoc_specific["Trombocite"]
        numar_globule = stoc_specific["Globule_rosii"]

        if numar_plasma - plasma >= 0 and numar_trombocite - tromobocite >= 0 and numar_globule - globule_rosii >= 0:
            self.service_sange.send_pungi(
                id_locatie_curenta, id_cerere, grupa, rh, plasma, tromobocite, globule_rosii)

            self.service_medic.update_cerere(id_cerere, 'Rezolvata')
            return 0, "Pungile au fost trimise"

        return 2, "Stoc insuficient."

    def get_analize(self, cnp):
        return self.service_sange.get_analize(cnp)

    def manage_request(self, formular_donare, id_locatie, analiza=None, staff_full_name=None):
        status = formular_donare.status
        id_donator = self.get_id_donator(formular_donare)

        if status.upper() == 'PRELEVARE':
            self.create_sange_brut(id_donator, id_locatie, staff_full_name)
        elif status.upper() == 'PREGATIRE':
            self.create_sange_prelucrat(id_donator)
        elif analiza is not None:
            alt = analiza.alt
            sif = analiza.sif
            antihtlv = analiza.antihtlv
            antihtcv = analiza.antihtcv
            antihiv = analiza.antihiv
            hb = analiza.hb
            grupa = formular_donare.grupa
            rh = formular_donare.rh
            self.create_analiza(id_donator, grupa, rh, alt, sif, antihtlv, antihtcv, antihiv, hb, formular_donare.id)

    def get_id_donator(self, formular_donare):
        return self.service_transfuzie.get_id_donator(formular_donare)

    def trimite_cerere_sange(self, cerere, cnp_medic):
        return self.service_medic.trimite_cerere_sange(cerere, cnp_medic)

    def get_istoric_donari(self, username):
        return self.service_donator.get_istoric_donari(username)
    
    def get_cereri_sange(self, id_locatie, status, from_spital):
        return self.service_medic.get_cereri_sange(id_locatie, status, from_spital)

    def anulare_cerere(self, id_cerere):
        return self.service_medic.update_cerere(id_cerere, 'Anulata')

    def is_a_valid_donation(self, cnp_donator):
        return self.service_donator.is_a_valid_donation(cnp_donator)


    def get_centru_home_screen_data(self, id_locatie):
        return self.service_sange.get_centru_home_screen_data(id_locatie)

    def get_stare_actuala(self, id_locatie):
        return self.service_medic.get_stare_actuala(id_locatie)

    def get_active_users(self):
        return self.service_chat.get_active_users()

    def add_active_user(self, user):
        return self.service_chat.add_active_user(user)

    def remove_user(self, user):
        return self.service_chat.remove_user(user)

    def add_new_message(self, sender, receiver, message, date):
        return self.service_chat.add_new_message(sender, receiver, message, date)

    def get_messages_for_user(self, user1, user2):
        return self.service_chat.get_messages_for_user(user1,user2)

