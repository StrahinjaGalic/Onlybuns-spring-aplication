package com.onlybuns.onlybuns.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Dto.AnalyticsDto;
import com.onlybuns.onlybuns.Model.Comment;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Repository.CommentRepository;
import com.onlybuns.onlybuns.Repository.PostRepository;
import com.onlybuns.onlybuns.Repository.UserRepository;


@Service
public class AnalyticsService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    
    public AnalyticsDto getAnalytics() {
        List<Post> posts = postRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        List<User> users = userRepository.findAll();

        LocalDate now = LocalDate.now();

        long postsWeek = posts.stream().filter(p -> p.getCreationTime().toLocalDate().isAfter(now.minusWeeks(1))).count();
        long postsMonth = posts.stream().filter(p -> p.getCreationTime().toLocalDate().isAfter(now.minusMonths(1))).count();
        long postsYear = posts.stream().filter(p -> p.getCreationTime().toLocalDate().isAfter(now.minusYears(1))).count();

        long commentsWeek = comments.stream().filter(c -> c.getCommentDate().toLocalDate().isAfter(now.minusWeeks(1))).count();
        long commentsMonth = comments.stream().filter(c -> c.getCommentDate().toLocalDate().isAfter(now.minusMonths(1))).count();
        long commentsYear = comments.stream().filter(c -> c.getCommentDate().toLocalDate().isAfter(now.minusYears(1))).count();

        Set<String> usersWithPosts = posts.stream().map(p -> p.getUsername()).collect(Collectors.toSet());
        Set<String> usersWithComments = comments.stream().map(c -> c.getUsername()).collect(Collectors.toSet());

        Set<String> usersWithOnlyComments = new HashSet<>(usersWithComments);
        usersWithOnlyComments.removeAll(usersWithPosts);

        Set<String> allUsernames = users.stream().map(u -> u.getUsername()).collect(Collectors.toSet());
        Set<String> usersWithPostsOrComments = new HashSet<>(usersWithPosts);
        usersWithPostsOrComments.addAll(usersWithComments);
        Set<String> usersWithoutPostsOrComments = new HashSet<>(allUsernames);
        usersWithoutPostsOrComments.removeAll(usersWithPostsOrComments);

        int totalUsers = allUsernames.size();

        double percentWithPosts = totalUsers == 0 ? 0 : (usersWithPosts.size() * 100.0) / totalUsers;
        double percentWithOnlyComments = totalUsers == 0 ? 0 : (usersWithOnlyComments.size() * 100.0) / totalUsers;
        double percentWithoutPostsOrComments = totalUsers == 0 ? 0 : (usersWithoutPostsOrComments.size() * 100.0) / totalUsers;

        return new AnalyticsDto(
            postsWeek,
            postsMonth,
            postsYear,
            commentsWeek,
            commentsMonth,
            commentsYear,
            percentWithPosts,
            percentWithOnlyComments,
            percentWithoutPostsOrComments
        );
    }
    
}
