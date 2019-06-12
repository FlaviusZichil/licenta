package app.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.documents.Article;
import app.documents.ArticleComment;
import app.documents.ArticleLike;
import app.dto.ArticleCommentDTO;
import app.dto.ArticleDTO;
import app.dto.ArticleLikeDTO;
import app.dto.CityDTO;
import app.dto.GuideDTO;
import app.dto.MountainDTO;
import app.dto.PeakDTO;
import app.dto.RouteDTO;
import app.dto.TombolaResultsDTO;
import app.dto.TripDTO;
import app.entities.Guide;
import app.entities.Peak;
import app.entities.Tombola;
import app.entities.Trip;

@Component
public class Conversion {
	
	@Autowired
	private ArticleUtils articleUtils;
	
	@Autowired
	private UserUtils userUtils;
	
	public ArticleDTO convertFromArticleToArticleDTO(Article article) {
		List<ArticleCommentDTO> commentsDTO = new ArrayList<>();
		List<ArticleLikeDTO> likesDTO = new ArrayList<>();
		
		for(ArticleComment comment : article.getComments()) {
			commentsDTO.add(convertFromArticleCommentToArticleCommentDTO(comment));
		}
		
		for(ArticleLike like : article.getLikes()) {
			likesDTO.add(convertFromArticleLikeToArticleLikeDTO(like));
		}
		
		Collections.reverse(commentsDTO);
		
		ArticleDTO articleDTO = new ArticleDTO(article.getArticleId(), userUtils.getUserById(article.getUserId()), article.getDate(), article.getTitle(), likesDTO, 
											   article.getDescription(), article.getSections(), commentsDTO);
		return articleDTO;
	}
	
	public ArticleCommentDTO convertFromArticleCommentToArticleCommentDTO(ArticleComment comment) {
		LocalDate articleDate = LocalDate.parse(comment.getDate());
		Period period = Period.between(LocalDate.now(), articleDate);
	    int days = Math.abs(period.getDays());
	    
		ArticleCommentDTO commentDTO = new ArticleCommentDTO(comment.getCommentId(), userUtils.getUserById(comment.getUserId()), comment.getDate(), comment.getContent(), days);
		return commentDTO;
	}
	
	public ArticleLikeDTO convertFromArticleLikeToArticleLikeDTO(ArticleLike like) {
		ArticleLikeDTO articleLikeDTO = new ArticleLikeDTO(userUtils.getUserById(like.getUserId()), articleUtils.getArticleById(like.getArticleId()));
		return articleLikeDTO;
	}
	
	public GuideDTO convertFromGuideToGuideDTO(Guide guide) {
		GuideDTO guideDTO = new GuideDTO(guide.getId(), guide.getUser(), guide.getYearsOfExperience(), guide.getPhoneNumber(), guide.getDescription());
		return guideDTO;
	}
	
	public TripDTO convertFromTripToTripDTO(Trip trip) {
		PeakDTO peakDTO = convertFromPeakToPeakDTO(trip.getPeak());
		GuideDTO guideDTO = null;
		if(!trip.getStatus().equals("Suggested")) {
			guideDTO = new GuideDTO(trip.getGuide().getId(), trip.getGuide().getUser(), trip.getGuide().getYearsOfExperience(), trip.getGuide().getPhoneNumber(), trip.getGuide().getDescription());
		}
		RouteDTO routeDTO = new RouteDTO(trip.getRoute().getId(), trip.getRoute().getDifficulty(), TripUtils.getRoutePointsDTOForTrip(trip));
		
		TripDTO tripDTO = new TripDTO(trip.getId(), trip.getCapacity(), trip.getStartDate(), trip.getEndDate(), trip.getStatus(), trip.getPoints(),
									  trip.getUserTrips(), routeDTO, peakDTO, guideDTO);
		return tripDTO;
	}
	
	public PeakDTO convertFromPeakToPeakDTO(Peak peak) {
		CityDTO cityDTO = new CityDTO(peak.getCity().getName(), peak.getCity().getLatitude(), peak.getCity().getLongitude());
		MountainDTO mountainDTO = new MountainDTO(peak.getMountain().getMountainName());
		PeakDTO peakDTO = new PeakDTO(peak.getId(), peak.getPeakName(), peak.getAltitude(), cityDTO, mountainDTO, peak.getTrips());
		
		return peakDTO;
	}
}
