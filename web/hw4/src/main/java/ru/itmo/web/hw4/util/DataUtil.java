package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final List<User> USERS = Arrays.asList(
            new User(1, "MikeMirzayanov", "Mike Mirzayanov"),
            new User(6, "pashka", "Pavel Mavrin"),
            new User(9, "geranazavr555", "Georgiy Nazarov"),
            new User(11, "tourist", "Gennady Korotkevich")
    );

    private static final List<Post> POSTS = Arrays.asList(
            new Post(2, 1,"Cats", "The cat (Felis catus) is a domestic species of small carnivorous mammal. " +
                    "It is the only domesticated species in the family Felidae and is commonly referred to as the domestic cat or house cat to distinguish it from the wild members of the family. " +
                    "A cat can either be a house cat, a farm cat, or a feral cat; the latter ranges freely and avoids human contact. " +
                    "Domestic cats are valued by humans for companionship and their ability to kill rodents. About 60 cat breeds are recognized by various cat registries.\n" +
                    "\n" +
                    "The cat is similar in anatomy to the other felid species: it has a strong flexible body, quick reflexes, sharp teeth, and retractable claws adapted to killing small prey. " +
                    "Its night vision and sense of smell are well developed. Cat communication includes vocalizations like meowing, purring, trilling, hissing, growling, and grunting as well as cat-specific body language. " +
                    "A predator that is most active at dawn and dusk (crepuscular), the cat is a solitary hunter but a social species. It can hear sounds too faint or too high in frequency for human ears, such as those made by mice and other small mammals. " +
                    "Cats also secrete and perceive pheromones."),
            new Post(7, 6, "Dogs", "The dog (Canis familiaris or Canis lupus familiaris) is a domesticated descendant of the wolf. " +
                    "Also called the domestic dog, it is derived from an ancient, extinct wolf, and the modern wolf is the dog's nearest living relative. " +
                    "The dog was the first species to be domesticated, by hunter-gatherers over 15,000 years ago, before the development of agriculture. " +
                    "Due to their long association with humans, dogs have expanded to a large number of domestic individuals and gained the ability to thrive on a starch-rich diet that would be inadequate for other canids."),
            new Post(10, 9, "Fish", "Fish fish fish fish fish fish fish fish fish fish fish fish fish fish fish fish fish fish fishvvv fish fishv fish fish fish fishvv fish"),
            new Post(12, 11,"Hamsters", "Hamsters are rodents (order Rodentia) belonging to the subfamily Cricetinae, " +
                    "which contains 19 species classified in seven genera. They have become established as popular small pets. " +
                    "The best-known species of hamster is the golden or Syrian hamster (Mesocricetus auratus), which is the type most commonly kept as pets. " +
                    "Other hamster species commonly kept as pets are the three species of dwarf hamster, Campbell's dwarf hamster (Phodopus campbelli), the winter white dwarf hamster " +
                    "(Phodopus sungorus) and the Roborovski hamster (Phodopus roborovskii).\n" +
                    "\n" +
                    "Hamsters are more crepuscular than nocturnal and, in the wild, remain underground during the day to avoid being caught by predators. They feed primarily on seeds, fruits, and vegetation, and will occasionally eat burrowing insects. " +
                    "Physically, they are stout-bodied with distinguishing features that include elongated cheek pouches extending to their shoulders, which they use to carry food back to their burrows, as well as a short tail and fur-covered feet."),
            new Post(159, 9,"Rabbits", "Rabbits, also known as bunnies or bunny rabbits, are small mammals in the family Leporidae (which also contains the hares) of the order Lagomorpha (which also contains the pikas). " +
                    "Oryctolagus cuniculus includes the European rabbit species and its descendants, the world's 305 breeds of domestic rabbit. " +
                    "Sylvilagus includes 13 wild rabbit species, among them the seven types of cottontail. " +
                    "The European rabbit, which has been introduced on every continent except Antarctica, is familiar throughout the world as a wild prey animal and as a domesticated form of livestock and pet. " +
                    "With its widespread effect on ecologies and cultures, the rabbit is, in many areas of the world, a part of daily life—as food, clothing, a companion, and a source of artistic inspiration.")
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("users", USERS);
        data.put("posts", POSTS);
        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}
