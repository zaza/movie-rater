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
      while line = file.gets
        # TODO: parse
        puts line
      end
    end
  end
  return movies
end

def extractMovieName(dir)
  while dir =~ /([A-Z][a-z]{2,})([A-Z][a-z]+)/
    dir = dir.gsub($1+''+$2, $1+' '+$2)
  end
  dir = dir.downcase
  dir = dir.gsub('dd5.1','')
  dir = dir.gsub('r5.line','')
  dir = dir.gsub('.',' ')
  dir = dir.gsub('_',' ')
  dir = dir.gsub(/\[.*\].*/,'')
  dir = dir.gsub(/\{.*\}.*/,'')
  dir = dir.gsub(/\(.*\).*/,'')
  dir = dir.gsub(/19|20[0-9]{2}/,'')
  dir = dir.gsub(/[0-9]+-[0-9]+/,'')
  dir = dir.gsub('-diamond','')
  dir = dir.gsub('ac3','')
  dir = dir.gsub('r5','')
  dir = dir.gsub('dvdrip','')
  dir = dir.gsub('dvd','')
  dir = dir.gsub('po polsku','')
  dir = dir.gsub('lektor','')
  dir = dir.gsub('french','')
  dir = dir.gsub(' pl','')
  dir = dir.gsub('dvbrip','')
  dir = dir.gsub('brrip','')
  dir = dir.gsub('rip','')
  dir = dir.gsub('xvid-alliance','')
  dir = dir.gsub('xvid-er','')
  dir = dir.gsub('xvid-ktks','')
  dir = dir.gsub('xvid','')
  dir = dir.gsub('divx','')
  dir = dir.gsub('ac3-vision','')
  dir = dir.gsub('ac3','')
  dir = dir.gsub('rmvbhunters','')
  dir = dir.gsub('rmvb','')
  dir = dir.gsub('avi','')
  dir = dir.gsub('subbed','')
  dir = dir.gsub('dub','')
  dir = dir.gsub(' 3d','')
  dir = dir.gsub('480p','')
  dir = dir.gsub('720p','')
  dir = dir.gsub(' !','')
  dir = dir.gsub(' scr','')
  dir = dir.gsub('bbs','')
  dir = dir.gsub('-fxg','')
  dir = dir.gsub('-fxm','')
  dir = dir.gsub('-hefty','')
  dir = dir.gsub('-axxo','')
  dir = dir.gsub('-xan-0','')
  dir = dir.gsub('-konik','')
  dir = dir.gsub('-80m','')
  dir = dir.gsub('-ruby','')
  dir = dir.gsub('-kjs','')
  dir = dir.gsub('-done','')
  dir = dir.gsub('-mdx','')
  dir = dir.gsub('-bestdivx','')
  dir = dir.gsub('-m00dy','')
  dir = dir.gsub('-psig','')
  dir = dir.gsub('-cinefile','')
  dir = dir.gsub('drp','')
  dir = dir.gsub('proper','')
  dir = dir.gsub('untouched','')
  dir = dir.gsub('unrated','')
  dir = dir.gsub('domino','')
  dir = dir.gsub('x264','')
  dir = dir.gsub('limited','')
  dir = dir.gsub('bigfags','')
  dir = dir.gsub('koniec','')
  dir = dir.gsub('thiller','')
  dir = dir.gsub('horror','')
  dir = dir.gsub('komedia rom','')
  dir = dir.gsub('director cut','')
  dir = dir.gsub('720p','')
  dir = dir.gsub('bluray','')
  dir = dir.gsub('blu ray','')
  dir = dir.gsub('p24','')
  dir = dir.gsub('readnfo','')
  dir = dir.gsub(' cz ','')
  dir = dir.gsub('-',' ')
  return dir.strip
end

class String
  def to_ascii
    cep = "\271\346\352\263\361\363\234\277\237\245\306\312\243\321\323\214\257\217"
    ascii = "acelnoszzACELNOSZZ"
#   s = Iconv.new("cp1250", "UTF-8").iconv(self)
    self.tr(cep,ascii)
  end
end

def scanDirs(path)
  dirs_hash = {}
  excludes = ARGV[1].split(",")
  if File.directory?(path)
    Find.find(path) do |f|
      # TODO: hardcoded path
      if f =~ /^f:\/media\/movies\/([a-zA-Z- ,]+)\/(.+)$/
        if !excludes.include?($1) && FileTest.directory?(f)
          dirs = dirs_hash[$1]
          if dirs.nil?
            dirs = []
          end
          dirs << $2.to_ascii
          dirs_hash[$1] = dirs
        end
      end
    end
  end
  return dirs_hash
end

def getMovieRating(dirs_hash)

  url = 'http://www.filmweb.pl'
  agent = WWW::Mechanize.new
  page = agent.get(url)
  link = page.links.find{ |l| l.text == 'przejdź do Filmweb.pl' }
  if !link.nil?
    page = agent.click(link)
  end

  link = page.links.find{ |l| l.text == 'Zaloguj się' }
  page = agent.click(link)

  credentials = ARGV[0].split('@')

  zaloguj_form = page.form('form')
  zaloguj_form.j_username = credentials[0]
  zaloguj_form.j_password = credentials[1]
  page = agent.submit(zaloguj_form)

  movies_hash = {}

  dirs_hash.each_pair do |category, dirs|
    puts "======================================> Processing category: #{category}"
    for dir in dirs
      title = ''
      rating = 0

      dir2 = extractMovieName(dir)
      print "Looking for '" + dir2 + "'... "
      $\ = "\n"
      
      # http://www.filmweb.pl/search/film
      szukaj_form = page.forms[0]
      szukaj_form.q = dir2
      page = agent.submit(szukaj_form)
      # or use "?q=<dir2>&amp;alias=film"

      # default
      item = Item.new(dir, '', 0.0, category)

      firstResult_a = page.search("//a[@class='searchResultTitle']")[0]
      if !firstResult_a.nil?
        title = firstResult_a.inner_text.strip
        firstSpanResultRating = page.search("//div[@class='searchResultRating']/span")[0];
        text = firstSpanResultRating.inner_html
        if text =~ /(ocena: )([0-9]{1}\.[0-9]{1,2})/
          item = Item.new(dir, title, $2.to_f, category)
        end  
      end
      print item
      $\ = nil
      movies_hash[dir] = item
    end
  end
  return movies_hash
end

#movies = readFile("movies.txt")
#puts "movies=" + movies.size.to_s
dirs_hash = scanDirs("f:/media/movies")
puts "categories=" + dirs_hash.size.to_s
# check in 'movies' first
#  movies[d] = i
  # if not found check on filmweb || if found but 3 months old
movies_hash = getMovieRating(dirs_hash)
    # if found add to 'movies.txt' with rating
    # else add to 'movies.txt' without rating
  # sort by rating, those with rating first
puts "======================================> Sorting"
items_sorted = movies_hash.values.sort

  # dump movies to file
  # print out results (if specified, narrow to category)
puts items_sorted
puts "Done."