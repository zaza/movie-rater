require 'rubygems'
require 'hpricot'
#http://theshed.hezmatt.org/mattshacks/ruby-mechanize/trunk/GUIDE.txt
require 'mechanize'
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
  dirs_hash = {}
  if File.directory?(path)
    # exclude
    Find.find(path) do |f|
      if f =~ /^d:\/temp\/movies\/([a-zA-Z]+)\/(.+)$/
        dirs = dirs_hash[$1]
        if dirs.nil?
          dirs = []
        end
        dirs << $2
        dirs_hash[$1] = dirs 
      end
    end
  end
  p dirs_hash
  return dirs_hash
end

def getMovieRatingFromImdb(dir)
  puts "Looking for '" + dir +"'..."
  title = extractMovieName(dir)
  url = 'http://www.imdb.com/find?s=tt&q='+title
  agent = WWW::Mechanize.new
  page = agent.get(url)
  pp page
  page.search("//b").each do |b|
    if b.inner_html == 'Popular titles'
      pp b.parent
    end
  end
end

def getMovieRatingFromFilmweb(dir)
 
  puts "Looking for '" + dir +"'..."
  title = extractMovieName(dir)
  #http://www.filmweb.pl/szukaj?q=xxx&c=film
  #url = 'http://www.filmweb.pl/szukaj?q='+title+'&c=film'
  url = 'http://www.filmweb.pl'
  agent = WWW::Mechanize.new
  page = agent.get(url)
  link = page.links.find{ |l| l.text == 'przejdź do Filmweb.pl' }
#  link = page.links.text('przejdź do Filmweb.pl')
#link = page.links.text('Zaloguj się')

  #puts link
  page = agent.click(link)
  
  link = page.links.find{ |l| l.text == 'Zaloguj się' }
  page = agent.click(link)
  
  zaloguj_form = page.form('form')
  zaloguj_form.j_username = 'zazo7'
  zaloguj_form.j_password='23ywtgx7g5w7'
  page = agent.submit(zaloguj_form)
  
  szukaj_form = page.forms[0]
  szukaj_form.q = dir
  szukaj_form.c = 'film'
  page = agent.submit(szukaj_form)
 
#  pp page
  
  firstResult_a = page.search("//a[@class='searchResultTitle']")[0]
  title = firstResult_a.inner_html
  span = page.search("//span[@style='color:#333; font-size: 0.9em; text-decoration: none;']")[2]
  text = span.inner_html
  if text =~ /(ocena:)([0-9]+\.[0-9]{2})/
    p title + " > "+ $2
  end
  #sleep 15
  ##p page.body
  #doc = Hpricot(page)
  #puts doc
  #span = doc.at("span[text*='ocena']")
  #p "====================================="
  #puts span
  rating = 0 # default
  #p "====================================="
  return Item.new(dir, title, rating)
end

movies = readFile("movies.txt")
puts "movies=" + movies.size.to_s
dirs = scanDirs("d:/temp/movies")
puts "dirs=" + dirs.size.to_s
for d in dirs
  # check in 'movies' first
#  i = getMovieRatingFromFilmweb(d)# unless movies.has_key?(d)
#  movies[d] = i
  # if not found check on filmweb
    # if found add to 'movies.txt' with rating
    # else add to 'movies.txt' without rating
  # sort by rating, those with rating first
  # dump movies to file
  # print out results
end
puts "Done."

