package ir.desibell.notificationService.domain.mail;

public class MailHtmlTemplate {

    public MailHtmlTemplate() {
    }

    public static String resetPasswordHtml(String link) {
        String htmlTemplate = "<!DOCTYPE html>\n"
                + "\n"
                + "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n"
                + "\n"
                + "<head>\n"
                + "	<title></title>\n"
                + "	<meta charset=\"utf-8\" />\n"
                + "	<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\" />\n"
                + "	<!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]-->\n"
                + "	<!--[if !mso]><!-->\n"
                + "	<link href=\"https://fonts.googleapis.com/css?family=Bitter\" rel=\"stylesheet\" type=\"text/css\" />\n"
                + "	<link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" type=\"text/css\" />\n"
                + "	<link href=\"https://fonts.googleapis.com/css?family=Varela+Round\" rel=\"stylesheet\" type=\"text/css\" />\n"
                + "	<!--<![endif]-->\n"
                + "	<style>\n"
                + "		* {\n"
                + "			box-sizing: border-box;\n"
                + "		}\n"
                + "\n"
                + "		body {\n"
                + "			margin: 0;\n"
                + "			padding: 0;\n"
                + "		}\n"
                + "\n"
                + "		th.column {\n"
                + "			padding: 0\n"
                + "		}\n"
                + "\n"
                + "		a[x-apple-data-detectors] {\n"
                + "			color: inherit !important;\n"
                + "			text-decoration: inherit !important;\n"
                + "		}\n"
                + "\n"
                + "		#MessageViewBody a {\n"
                + "			color: inherit;\n"
                + "			text-decoration: none;\n"
                + "		}\n"
                + "\n"
                + "		p {\n"
                + "			line-height: inherit\n"
                + "		}\n"
                + "\n"
                + "		@media (max-width:620px) {\n"
                + "			.icons-inner {\n"
                + "				text-align: center;\n"
                + "			}\n"
                + "\n"
                + "			.icons-inner td {\n"
                + "				margin: 0 auto;\n"
                + "			}\n"
                + "\n"
                + "			.fullMobileWidth,\n"
                + "			.row-content {\n"
                + "				width: 100% !important;\n"
                + "			}\n"
                + "\n"
                + "			.image_block img.big {\n"
                + "				width: auto !important;\n"
                + "			}\n"
                + "\n"
                + "			.stack .column {\n"
                + "				width: 100%;\n"
                + "				display: block;\n"
                + "			}\n"
                + "\n"
                + "			.reverse {\n"
                + "				display: table;\n"
                + "				width: 100%;\n"
                + "			}\n"
                + "\n"
                + "			.reverse th.first {\n"
                + "				display: table-footer-group !important;\n"
                + "			}\n"
                + "\n"
                + "			.reverse th.last {\n"
                + "				display: table-header-group !important;\n"
                + "			}\n"
                + "\n"
                + "			.row-13 th.column.first>table,\n"
                + "			.row-13 th.column.last>table {\n"
                + "				padding-left: 15px;\n"
                + "				padding-right: 15px;\n"
                + "			}\n"
                + "		}\n"
                + "	</style>\n"
                + "</head>\n"
                + "\n"
                + "<body style=\"background-color: #080324; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n"
                + "	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\"\n"
                + "		style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #080324;\" width=\"100%\">\n"
                + "		<tbody>\n"
                + "			<tr>\n"
                + "				<td>\n"
                + "					<table class=\"row row-13\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n"
                + "						width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\n"
                + "						<th class=\"column\"\n"
                + "							style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: center; vertical-align: top; padding-left: 15px; padding-right: 15px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n"
                + "							width=\"50%\">\n"
                + "							<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"image_block\" role=\"presentation\"\n"
                + "								style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n"
                + "								<tr>\n"
                + "									<td\n"
                + "										style=\"padding-bottom:25px;padding-left:10px;padding-right:10px;padding-top:25px;width:100%;\">\n"
                + "										<div align=\"center\" style=\"line-height:10px\"><img alt=\"Start Trading Main Image\"\n"
                + "												src=\"https://s4.uupload.ir/files/v1.22c_o76w.png\"\n"
                + "												style=\"display: block; height: auto; border: 0; width: 110px; max-width: 100%;\"\n"
                + "												title=\"Start Trading Main Image\" width=\"250\" />\n"
                + "											<br>\n"
                + "											<br>\n"
                + "											<br>\n"
                + "											<div style=\"line-height:10px\" align=\"center\">\n"
                + "												<span\n"
                + "													style=\"font-family:Varela Round, Trebuchet MS, Helvetica, sans-serif;font-size:18px;color: #ffffff;\">DesiBell.ir</span>\n"
                + "											</div>\n"
                + "\n"
                + "										</div>\n"
                + "									</td>\n"
                + "								</tr>\n"
                + "							</table>\n"
                + "						</th>\n"
                + "						<tbody>\n"
                + "							<tr>\n"
                + "								<td>\n"
                + "									<table class=\"row-content stack\" role=\"presentation\"\n"
                + "										style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"600\"\n"
                + "										cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\">\n"
                + "										<tbody>\n"
                + "											<tr class=\"reverse\">\n"
                + "												<th class=\"column last\"\n"
                + "													style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-left: 15px; padding-right: 15px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n"
                + "													width=\"50%\">\n"
                + "													<table class=\"text_block\" role=\"presentation\"\n"
                + "														style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\"\n"
                + "														width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n"
                + "														<tbody>\n"
                + "															<tr>\n"
                + "																<td\n"
                + "																	style=\"padding-bottom:15px;padding-left:15px;padding-right:15px;padding-top:50px;text-align: justify;text-align-last: center;\">\n"
                + "																	<div style=\"font-family: sans-serif\">\n"
                + "																		<div\n"
                + "																			style=\"font-size: 14px; color: #ffffff; line-height: 1.5; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\">\n"
                + "																			<p\n"
                + "																				style=\"margin: 0; font-size: 14px; text-align: left; mso-line-height-alt: 24px;\">\n"
                + "																				<span\n"
                + "																					style=\"font-size:16px; text-align: right;\">\n"
                + "																					یک درخواست بازنشانی رمز عبور برای\n"
                + "																					شما ایجاد شده است\n"
                + "																					<br>\n"
                + "																					برای تغییر رمز عبور بر روی دکمه زیر\n"
                + "																					کلیک کنید\n"
                + "																					<br>\n"
                + "																					یا اگر روی دستگاه دیگری هستید، لینک\n"
                + "																					زیر را کپی کنید\n"
                + "																					<br>\n"
                + "																				</span>\n"
                + "																			</p>\n"
                + "																		</div>\n"
                + "																	</div>\n"
                + "																</td>\n"
                + "															</tr>\n"
                + "														</tbody>\n"
                + "													</table>\n"
                + "													<table class=\"button_block\" role=\"presentation\"\n"
                + "														style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n"
                + "														width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n"
                + "														<tbody>\n"
                + "															<tr>\n"
                + "																<td\n"
                + "																	style=\"padding-bottom:15px;padding-left:10px;padding-right:10px;padding-top:10px;text-align:left;text-align: justify;text-align-last: center;\">\n"
                + "																	<!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"http://www.example.com\" style=\"height:42px;width:145px;v-text-anchor:middle;\" arcsize=\"39%\" stroke=\"false\" fillcolor=\"#fecf07\"><w:anchorlock/><v:textbox inset=\"0px,0px,0px,0px\"><center style=\"color:#080324; font-family:sans-serif; font-size:16px\"><![endif]--><a\n"
                + "																		href=\"" + link + "\"\n"
                + "																		style=\"text-decoration:none;display:inline-block;color:#080324;background-color:#fecf07;border-radius:16px;width:auto;border-top:0px solid TRANSPARENT;border-right:0px solid TRANSPARENT;border-bottom:0px solid TRANSPARENT;border-left:0px solid TRANSPARENT;padding-top:5px;padding-bottom:5px;font-family:Varela Round, Trebuchet MS, Helvetica, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\"\n"
                + "																		target=\"_blank\"><span\n"
                + "																			style=\"padding-left:25px;padding-right:25px;font-size:16px;display:inline-block;letter-spacing:normal;\"><span\n"
                + "																				style=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><strong>تغییر\n"
                + "																					رمز عبور</strong></span></span></a>\n"
                + "																	<!--[if mso]></center></v:textbox></v:roundrect><![endif]-->\n"
                + "																	<div\n"
                + "																		style=\"font-size: 14px; color: #ffffff; line-height: 1.5; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\">\n"
                + "																		<p\n"
                + "																			style=\"margin: 0; font-size: 14px; text-align: left; mso-line-height-alt: 24px;\">\n"
                + "																			<span\n"
                + "																				style=\"font-size:16px; text-align: right;\">\n"
                + "																					 \n<br><br>" + "<div>" + link + "</div>"
                + "																			</span>\n"
                + "																		</p>\n"
                + "																	</div>\n"
                + "																	<div\n"
                + "																		style=\"font-size: 14px; color: #ffffff; line-height: 1.5; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\">\n"
                + "																		<p\n"
                + "																			style=\"margin: 0; font-size: 14px; text-align: left; mso-line-height-alt: 24px;\">\n"
                + "																			<span\n"
                + "																				style=\"font-size:16px; text-align: right;\">\n"
                + "																				<br>\n"
                + "																				<br>\n"
                + "																				اگر این درخواست شما نیست، لطفاً این\n"
                + "																				ایمیل را نادیده بگیرید\n"
                + "																			</span>\n"
                + "																		</p>\n"
                + "																	</div>\n"
                + "																</td>\n"
                + "															</tr>\n"
                + "														</tbody>\n"
                + "													</table>\n"
                + "\n"
                + "												</th>\n"
                + "											</tr>\n"
                + "										</tbody>\n"
                + "									</table>\n"
                + "								</td>\n"
                + "							</tr>\n"
                + "						</tbody>\n"
                + "					</table>\n"
                + "\n"
                + "					<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-14\"\n"
                + "						role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n"
                + "						<tbody>\n"
                + "							<tr>\n"
                + "								<td>\n"
                + "									<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n"
                + "										class=\"row-content stack\" role=\"presentation\"\n"
                + "										style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-position: top center; background-repeat: no-repeat; background-image: url('images/background_bottom.png');\"\n"
                + "										width=\"600\">\n"
                + "										<tbody>\n"
                + "											<tr>\n"
                + "												<th class=\"column\"\n"
                + "													style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n"
                + "													width=\"100%\">\n"
                + "													<table border=\"0\" cellpadding=\"10\" cellspacing=\"0\"\n"
                + "														class=\"divider_block\" role=\"presentation\"\n"
                + "														style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n"
                + "														width=\"100%\">\n"
                + "														<tr>\n"
                + "															<td>\n"
                + "																<div align=\"center\">\n"
                + "																	<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n"
                + "																		role=\"presentation\"\n"
                + "																		style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n"
                + "																		width=\"100%\">\n"
                + "																		<tr>\n"
                + "																			<td class=\"divider_inner\"\n"
                + "																				style=\"font-size: 1px; line-height: 1px; border-top: 1px solid #282343;\">\n"
                + "																				<span></span>\n"
                + "																			</td>\n"
                + "																		</tr>\n"
                + "																	</table>\n"
                + "																</div>\n"
                + "															</td>\n"
                + "														</tr>\n"
                + "													</table>\n"
                + "													<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n"
                + "														class=\"image_block\" role=\"presentation\"\n"
                + "														style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n"
                + "														width=\"100%\">\n"
                + "														<tr>\n"
                + "															<td\n"
                + "																style=\"padding-bottom:10px;padding-left:10px;padding-right:10px;padding-top:30px;width:100%;\">\n"
                + "																<div align=\"center\" style=\"line-height:10px\">\n"
                + "																	<span\n"
                + "																		style=\"font-family:Varela Round, Trebuchet MS, Helvetica, sans-serif;font-size:18px;color: #ffffff;\">DesiBell.ir</span>\n"
                + "																</div>\n"
                + "															</td>\n"
                + "														</tr>\n"
                + "													</table>\n"
                + "													<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\"\n"
                + "														role=\"presentation\"\n"
                + "														style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\"\n"
                + "														width=\"100%\">\n"
                + "														<tr>\n"
                + "															<td\n"
                + "																style=\"padding-bottom:30px;padding-left:10px;padding-right:10px;padding-top:10px;\">\n"
                + "																<div style=\"font-family: sans-serif\">\n"
                + "																	<div\n"
                + "																		style=\"font-size: 12px; color: #7b7b7b; line-height: 1.2; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\">\n"
                + "																		<p\n"
                + "																			style=\"margin: 0; font-size: 14px; text-align: center;\">\n"
                + "																			<span style=\"font-size:12px;\">© 2021\n"
                + "																				DesiBell. All Rights Reserved.</span>\n"
                + "																		</p>\n"
                + "																	</div>\n"
                + "																</div>\n"
                + "															</td>\n"
                + "														</tr>\n"
                + "													</table>\n"
                + "												</th>\n"
                + "											</tr>\n"
                + "										</tbody>\n"
                + "									</table>\n"
                + "								</td>\n"
                + "							</tr>\n"
                + "						</tbody>\n"
                + "					</table>\n"
                + "\n"
                + "				</td>\n"
                + "			</tr>\n"
                + "		</tbody>\n"
                + "	</table><!-- End -->\n"
                + "</body>\n"
                + "\n"
                + "</html>";

        return htmlTemplate;
    }
}