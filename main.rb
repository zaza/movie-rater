require 'rubygems'
require 'hpricot'
require 'mechanize'
require 'open-uri'
require 'find'
require 'item'

def readFile(f)
  movies = {}
  # path -> parsed name, rating
  if File.exist?(f)
    File.open(f) do |file|
      while line=file.gets
        # TODO: parse
        puts line
      end
    end
  end
  #movies["moon"] = Item.new("moon","moon",9)
  return movies
end

def extractMovieName(dir)
  # TODO: remove (2009), R5, DVD, etc
  return dir.gsub(" ", "+")
end

def scanDirs(path)
  dirs = [] 
  if File.directory?(path)
    # exclude
    Find.find(path) do |f|
      if f =~ /^(d:\/temp\/movies\/[a-zA-Z-]+\/)(.+)$/
        dirs << $2 
      end
    end
  end
  return dirs
end

def getMovieRating(dir)
  puts "Looking for " + dir
  title = extractMovieName(dir)
  #http://www.filmweb.pl/szukaj?q=xxx&c=film
  url = 'http://www.filmweb.pl/szukaj?q='+title+'&c=film'
  agent = WWW::Mechanize.new
  page = agent.get(url)
  link = page.links.find{ |l| l.text == 'przejdź do Filmweb.pl' }
  #link = page.links.text('przejdź do Filmweb.pl'
  #puts link
  page = agent.click(link) 
  #sleep 15
  p page.body
  #doc = Hpricot(page)
  #puts doc
  #span = doc.at("span[text*='ocena']")
  p "====================================="
  #puts span
  rating = 0 # default
  p "====================================="
  return Item.new(dir, title, rating)
end

movies = readFile("movies.txt")
puts movies.size
dirs = scanDirs("d:/temp/movies")
for d in dirs
  # check in 'movies' first
  i = getMovieRating(d) unless movies.has_key?(d)
  movies[d] = i
  # if not found check on filmweb
    # if found add to 'movies.txt' with rating
    # else add to 'movies.txt' without rating
  # sort by rating, those with rating first
  # dump movies to file
  # print out results
end

